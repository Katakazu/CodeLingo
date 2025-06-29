package com.example.codelingo.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.codelingo.R
import com.example.codelingo.data.preferences.UserPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LearningFragment : Fragment() {
    companion object {
        private const val ARG_LESSON_INDEX = "lesson_index"
        fun newInstance(lessonIndex: Int): LearningFragment {
            val fragment = LearningFragment()
            val args = Bundle()
            args.putInt(ARG_LESSON_INDEX, lessonIndex)
            fragment.arguments = args
            return fragment
        }
    }

    // Multi-lesson data
    private val lessons = listOf(
        listOf(
            Question(
                text = "Jenis nilai yang dapat disimpan dalam sebuah variabel disebut ....",
                options = listOf("Jenis program", "Tipe data", "Codingan", "Penulisan"),
                correctIndex = 1,
                explanation = "Tipe data (data type) adalah jenis nilai yang dapat disimpan dalam sebuah variabel. Tipe data menentukan: bentuk nilainya (angka, huruf, teks, logika), ukuran memori yang digunakan, operasi apa saja yang bisa dilakukan terhadap nilai itu."
            ),
            Question(
                text = "Tipe data yang digunakan untuk menyimpan teks atau kumpulan karakter adalah ....",
                options = listOf("String", "Integer", "Boolean", "Double"),
                correctIndex = 0,
                explanation = "String menyimpan teks, dan biasanya dalam tanda kutip ganda."
            )
        ),
        listOf(
            Question(
                text = "Apa hasil dari 2 + 2 * 2?",
                options = listOf("6", "8", "4", "2"),
                correctIndex = 0,
                explanation = "Perkalian didahulukan, jadi 2 + (2*2) = 2 + 4 = 6."
            ),
            Question(
                text = "Tipe data untuk bilangan desimal di Java adalah ....",
                options = listOf("int", "float", "char", "boolean"),
                correctIndex = 1,
                explanation = "float digunakan untuk bilangan desimal di Java."
            )
        ),
        listOf(
            Question(
                text = "Apa keyword untuk membuat variabel konstan di Java?",
                options = listOf("final", "const", "static", "var"),
                correctIndex = 0,
                explanation = "final digunakan untuk variabel konstan di Java."
            ),
            Question(
                text = "Manakah yang termasuk tipe data boolean?",
                options = listOf("true/false", "angka", "teks", "karakter"),
                correctIndex = 0,
                explanation = "Boolean hanya memiliki dua nilai: true dan false."
            )
        )
    )

    private var currentQuestion = 0
    private var selectedOption = -1
    private var lives = 3
    private var sessionXp = 0
    private var lessonIndex = 0
    private val targetXp = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lessonIndex = arguments?.getInt(ARG_LESSON_INDEX, 0) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.learning_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionButton = view.findViewById<Button>(R.id.actionButton)
        actionButton.setBackgroundResource(R.drawable.button_orange_rounded)
        actionButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        showQuestion(view)
    }

    private fun showQuestion(view: View) {
        val questions = lessons[lessonIndex]
        val question = questions[currentQuestion]
        val questionText = view.findViewById<TextView>(R.id.questionText)
        val answersContainer = view.findViewById<LinearLayout>(R.id.answersContainer)
        val feedbackContainer = view.findViewById<LinearLayout>(R.id.feedbackContainer)
        val feedbackText = view.findViewById<TextView>(R.id.feedbackText)
        val actionButton = view.findViewById<Button>(R.id.actionButton)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val questionNumberText = view.findViewById<TextView>(R.id.questionNumberText)
        val lifeCountText = view.findViewById<TextView>(R.id.lifeCountText)

        // Set progress and question number
        progressBar.max = questions.size
        progressBar.progress = currentQuestion + 1
        questionNumberText.text = "${currentQuestion + 1}/${questions.size}"
        lifeCountText.text = lives.toString()

        // Set question
        questionText.text = question.text
        answersContainer.removeAllViews()
        feedbackContainer.visibility = View.GONE
        actionButton.text = "JAWAB"
        actionButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        selectedOption = -1

        // Add answer options
        for ((index, option) in question.options.withIndex()) {
            val btn = Button(requireContext())
            btn.text = option
            btn.setBackgroundResource(R.drawable.edit_text_background)
            btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
            btn.textSize = 16f
            btn.setPadding(16, 16, 16, 16)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 16)
            btn.layoutParams = params
            btn.setOnClickListener {
                selectedOption = index
                // Highlight selected
                for (i in 0 until answersContainer.childCount) {
                    val child = answersContainer.getChildAt(i) as Button
                    child.setBackgroundResource(R.drawable.edit_text_background)
                    child.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
                }
                btn.setBackgroundResource(R.drawable.button_orange_rounded)
                btn.setTextColor(Color.WHITE)
            }
            answersContainer.addView(btn)
        }

        actionButton.setOnClickListener {
            if (selectedOption == -1) {
                Toast.makeText(requireContext(), "Pilih salah satu jawaban!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Cek jawaban
            val isCorrect = selectedOption == question.correctIndex
            answersContainer.getChildAt(question.correctIndex).setBackgroundResource(R.drawable.button_white_rounded_border)
            answersContainer.getChildAt(question.correctIndex).setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            if (isCorrect) {
                feedbackText.text = "Kamu benar!\n\n${question.explanation}"
                // Tambah XP sesuai level
                val prefs = UserPreferences(requireContext())
                val level = prefs.getUserLevel()
                val xpPerCorrect = when {
                    level == 0 || level == 1 -> 50
                    level == 2 -> 40
                    level == 3 -> 30
                    level == 4 -> 20
                    level in 5..9 -> 10
                    else -> 5
                }
                sessionXp += xpPerCorrect
            } else {
                feedbackText.text = "Kurang tepat!\n\n${question.explanation}"
                lives--
                lifeCountText.text = lives.toString()
            }
            feedbackContainer.visibility = View.VISIBLE
            // Disable all options
            for (i in 0 until answersContainer.childCount) {
                answersContainer.getChildAt(i).isEnabled = false
            }
            actionButton.text = if (currentQuestion < questions.size - 1) "LANJUTKAN" else "SELESAI"
            actionButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            actionButton.setOnClickListener {
                if (currentQuestion < questions.size - 1) {
                    currentQuestion++
                    showQuestion(view)
                } else {
                    updateUserStats()
                    Toast.makeText(requireContext(), "Selesai!", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                }
            }
        }
    }

    private fun updateUserStats() {
        val prefs = UserPreferences(requireContext())
        
        // Debug print
        android.util.Log.d("LearningFragment", "Session XP: $sessionXp, Current Today XP: ${prefs.getTodayXp()}")
        
        // Tambah XP menggunakan method addXp yang sudah handle total, harian, dan user XP
        prefs.addXp(sessionXp)
        
        // Debug print setelah addXp
        android.util.Log.d("LearningFragment", "After addXp - Today XP: ${prefs.getTodayXp()}")
        
        // Update level jika perlu
        var xp = prefs.getUserXp()
        var level = prefs.getUserLevel()
        while (xp >= targetXp) {
            xp -= targetXp
            level += 1
        }
        prefs.setUserXp(xp)
        prefs.setUserLevel(level)
        
        // Tambah hari belajar hanya jika hari ini berbeda dari terakhir belajar
        val sdf = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault())
        val today = sdf.format(java.util.Date())
        val lastDay = prefs.getLastLearnDate()
        if (lastDay == null || lastDay != today) {
            val days = prefs.getUserDays() + 1
            prefs.setUserDays(days)
            prefs.setLastLearnDate(today)
        }
        
        // Progress lesson hanya bertambah jika semua soal benar (lives tidak berkurang)
        val currentLesson = prefs.getCurrentLesson()
        if (currentLesson < lessons.size && currentLesson == lessonIndex + 1 && lives == 3) {
            prefs.setCurrentLesson(currentLesson + 1)
            prefs.addLessonCompletedToday()
        }
    }
}

// Data class for question
private data class Question(
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
) 