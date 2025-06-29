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
import androidx.lifecycle.ViewModelProvider
import com.example.codelingo.R
import com.example.codelingo.data.preferences.UserPreferences
import com.example.codelingo.viewmodel.AuthViewModel
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
        // Lesson 1: Dasar-dasar Java
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
            ),
            Question(
                text = "Tipe data untuk bilangan bulat di Java adalah ....",
                options = listOf("int", "float", "char", "boolean"),
                correctIndex = 0,
                explanation = "int digunakan untuk menyimpan bilangan bulat di Java."
            )
        ),
        
        // Lesson 2: Operator dan Ekspresi
        listOf(
            Question(
                text = "Apa hasil dari 2 + 2 * 2?",
                options = listOf("6", "8", "4", "2"),
                correctIndex = 0,
                explanation = "Perkalian didahulukan, jadi 2 + (2*2) = 2 + 4 = 6."
            ),
            Question(
                text = "Operator yang digunakan untuk membandingkan kesamaan adalah ....",
                options = listOf("=", "==", "!=", ">"),
                correctIndex = 1,
                explanation = "== digunakan untuk membandingkan kesamaan, sedangkan = untuk assignment."
            ),
            Question(
                text = "Apa hasil dari 10 % 3?",
                options = listOf("3", "1", "0", "7"),
                correctIndex = 1,
                explanation = "Operator % adalah modulo yang memberikan sisa pembagian. 10 dibagi 3 = 3 sisa 1."
            )
        ),
        
        // Lesson 3: Variabel dan Konstanta
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
            ),
            Question(
                text = "Cara mendeklarasikan variabel yang benar adalah ....",
                options = listOf("int x = 5;", "x = 5;", "int x;", "var x = 5;"),
                correctIndex = 0,
                explanation = "Deklarasi variabel yang benar: tipe data + nama variabel + nilai (opsional)."
            )
        ),
        
        // Lesson 4: Struktur Kontrol - If Else
        listOf(
            Question(
                text = "Struktur kontrol yang digunakan untuk membuat keputusan adalah ....",
                options = listOf("if-else", "for", "while", "switch"),
                correctIndex = 0,
                explanation = "if-else digunakan untuk membuat keputusan berdasarkan kondisi tertentu."
            ),
            Question(
                text = "Apa output dari kode: if(5 > 3) System.out.println('Benar'); else System.out.println('Salah');",
                options = listOf("Benar", "Salah", "Error", "Tidak ada output"),
                correctIndex = 0,
                explanation = "Karena 5 > 3 adalah true, maka akan mencetak 'Benar'."
            ),
            Question(
                text = "Keyword 'else if' digunakan untuk ....",
                options = listOf("Mengakhiri program", "Menambah kondisi baru", "Mengulang kode", "Mendeklarasi variabel"),
                correctIndex = 1,
                explanation = "else if digunakan untuk menambah kondisi baru setelah if pertama."
            )
        ),
        
        // Lesson 5: Perulangan - For Loop
        listOf(
            Question(
                text = "Perulangan 'for' terdiri dari 3 bagian utama, yaitu ....",
                options = listOf("inisialisasi, kondisi, increment", "start, end, step", "begin, condition, end", "init, check, update"),
                correctIndex = 0,
                explanation = "For loop: inisialisasi (awal), kondisi (kapan berhenti), increment/decrement (perubahan)."
            ),
            Question(
                text = "Apa output dari: for(int i=1; i<=3; i++) System.out.print(i);",
                options = listOf("123", "321", "111", "333"),
                correctIndex = 0,
                explanation = "Loop akan berjalan 3 kali: i=1, i=2, i=3, sehingga outputnya 123."
            ),
            Question(
                text = "Keyword 'break' dalam perulangan berfungsi untuk ....",
                options = listOf("Mengulang kode", "Menghentikan perulangan", "Melanjutkan ke iterasi berikutnya", "Mengembalikan nilai"),
                correctIndex = 1,
                explanation = "break digunakan untuk menghentikan perulangan secara paksa."
            )
        ),
        
        // Lesson 6: Perulangan - While dan Do-While
        listOf(
            Question(
                text = "Perbedaan utama while dan do-while adalah ....",
                options = listOf("while lebih cepat", "do-while minimal 1x eksekusi", "while tidak bisa infinite", "do-while lebih sulit"),
                correctIndex = 1,
                explanation = "do-while minimal dieksekusi 1 kali, sedangkan while bisa tidak dieksekusi sama sekali."
            ),
            Question(
                text = "Apa output dari: int x=5; while(x>0) { System.out.print(x); x--; }",
                options = listOf("54321", "12345", "55555", "00000"),
                correctIndex = 0,
                explanation = "x dimulai dari 5, setiap iterasi x berkurang 1, sehingga output 54321."
            ),
            Question(
                text = "Keyword 'continue' dalam perulangan berfungsi untuk ....",
                options = listOf("Menghentikan perulangan", "Melanjutkan ke iterasi berikutnya", "Mengulang dari awal", "Mengembalikan nilai"),
                correctIndex = 1,
                explanation = "continue digunakan untuk melompat ke iterasi berikutnya tanpa menjalankan kode di bawahnya."
            )
        ),
        
        // Lesson 7: Array
        listOf(
            Question(
                text = "Array adalah struktur data yang menyimpan ....",
                options = listOf("Satu nilai", "Banyak nilai dengan tipe sama", "Banyak nilai dengan tipe berbeda", "Fungsi"),
                correctIndex = 1,
                explanation = "Array menyimpan banyak nilai dengan tipe data yang sama."
            ),
            Question(
                text = "Cara mendeklarasikan array yang benar adalah ....",
                options = listOf("int[] arr = {1,2,3};", "int arr = [1,2,3];", "array arr = {1,2,3};", "int arr = {1,2,3};"),
                correctIndex = 0,
                explanation = "Deklarasi array: tipe data[] nama = {nilai1, nilai2, ...};"
            ),
            Question(
                text = "Index array dimulai dari ....",
                options = listOf("0", "1", "-1", "Tergantung ukuran"),
                correctIndex = 0,
                explanation = "Index array di Java dimulai dari 0, bukan 1."
            )
        ),
        
        // Lesson 8: Method/Fungsi
        listOf(
            Question(
                text = "Method yang tidak mengembalikan nilai menggunakan keyword ....",
                options = listOf("return", "void", "null", "empty"),
                correctIndex = 1,
                explanation = "void digunakan untuk method yang tidak mengembalikan nilai."
            ),
            Question(
                text = "Parameter dalam method berfungsi untuk ....",
                options = listOf("Mengembalikan nilai", "Menerima input dari luar", "Menghentikan method", "Mengulang kode"),
                correctIndex = 1,
                explanation = "Parameter digunakan untuk menerima input dari luar method."
            ),
            Question(
                text = "Keyword 'return' berfungsi untuk ....",
                options = listOf("Menghentikan program", "Mengembalikan nilai", "Mengulang kode", "Mendeklarasi variabel"),
                correctIndex = 1,
                explanation = "return digunakan untuk mengembalikan nilai dari method."
            )
        ),
        
        // Lesson 9: Class dan Object
        listOf(
            Question(
                text = "Class adalah blueprint untuk membuat ....",
                options = listOf("Method", "Variabel", "Object", "Array"),
                correctIndex = 2,
                explanation = "Class adalah blueprint atau template untuk membuat object."
            ),
            Question(
                text = "Method yang dipanggil saat object dibuat adalah ....",
                options = listOf("main method", "constructor", "getter", "setter"),
                correctIndex = 1,
                explanation = "Constructor adalah method khusus yang dipanggil saat object dibuat."
            ),
            Question(
                text = "Keyword 'new' digunakan untuk ....",
                options = listOf("Mendeklarasi variabel", "Membuat object", "Mengembalikan nilai", "Menghentikan program"),
                correctIndex = 1,
                explanation = "new digunakan untuk membuat instance/object baru dari class."
            )
        ),
        
        // Lesson 10: Inheritance dan Polymorphism
        listOf(
            Question(
                text = "Inheritance memungkinkan class untuk ....",
                options = listOf("Mewarisi method dan property dari class lain", "Mengubah tipe data", "Membuat array", "Menghentikan program"),
                correctIndex = 0,
                explanation = "Inheritance memungkinkan class child mewarisi method dan property dari class parent."
            ),
            Question(
                text = "Keyword untuk inheritance di Java adalah ....",
                options = listOf("extends", "implements", "inherits", "super"),
                correctIndex = 0,
                explanation = "extends digunakan untuk inheritance, implements untuk interface."
            ),
            Question(
                text = "Polymorphism memungkinkan object untuk ....",
                options = listOf("Mengubah bentuk", "Berperilaku berbeda dalam situasi berbeda", "Menggandakan diri", "Menghilang"),
                correctIndex = 1,
                explanation = "Polymorphism memungkinkan object berperilaku berbeda dalam situasi yang berbeda."
            )
        )
    )

    private var currentQuestion = 0
    private var selectedOption = -1
    private var lives = 3
    private var sessionXp = 0
    private var lessonIndex = 0
    private val targetXp = 200
    private lateinit var authViewModel: AuthViewModel

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
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
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
                    // Update progress ke Firestore
                    val prefs = UserPreferences(requireContext())
                    authViewModel.updateUserProgress(
                        experience = prefs.getUserXp(),
                        level = prefs.getUserLevel(),
                        totalScore = prefs.getTotalXp()
                    )
                    authViewModel.updateUserLesson(prefs.getCurrentLesson())
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