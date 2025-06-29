package com.example.codelingo.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.codelingo.R
import com.google.android.material.card.MaterialCardView
import com.example.codelingo.data.preferences.UserPreferences
import android.graphics.Typeface
import android.view.Gravity
import android.widget.LinearLayout
import androidx.core.content.ContextCompat

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val totalLesson = 10 // Ganti sesuai jumlah lesson yang tersedia
        showUserStats(view, totalLesson)
        showLessonList(view, totalLesson)
        setupClickListeners(view, totalLesson)

        val prefs = UserPreferences(requireContext())
        val continueLearningButton = view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.continue_learning_button)
        val currentLesson = prefs.getCurrentLesson().coerceIn(1, totalLesson)
        continueLearningButton.text = if (currentLesson <= 1) "Mulai" else "Lanjut"
        continueLearningButton.setOnClickListener {
            val learningFragment = LearningFragment.newInstance(currentLesson - 1)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, learningFragment)
                .addToBackStack(null)
                .commit()
        }
    }
    
    private fun showUserStats(view: View, totalLesson: Int) {
        val prefs = UserPreferences(requireContext())
        val textLevel = view.findViewById<TextView>(R.id.textLevel)
        val textXp = view.findViewById<TextView>(R.id.textXp)
        val textDays = view.findViewById<TextView>(R.id.textDays)
        val textLessonProgress = view.findViewById<TextView>(R.id.textLessonProgress)
        val level = prefs.getUserLevel()
        val totalXp = prefs.getTotalXp()
        val targetTotalXp = 200 * (level + 1)
        val lesson = prefs.getCurrentLesson().coerceIn(1, totalLesson)
        textLevel.text = level.toString()
        textXp.text = "$totalXp/$targetTotalXp"
        textDays.text = prefs.getUserDays().toString()
        textLessonProgress.text = "Lesson $lesson dari $totalLesson"
    }

    private fun showLessonList(view: View, totalLesson: Int) {
        val prefs = UserPreferences(requireContext())
        val lessonListContainer = view.findViewById<LinearLayout>(R.id.lessonListContainer)
        lessonListContainer.removeAllViews()
        val unlocked = prefs.getCurrentLesson().coerceIn(1, totalLesson)
        for (i in 1..totalLesson) {
            val btn = TextView(requireContext())
            btn.text = "Lesson $i"
            btn.textSize = 16f
            btn.setTypeface(null, Typeface.BOLD)
            btn.setPadding(32, 24, 32, 24)
            btn.gravity = Gravity.CENTER_VERTICAL
            btn.background = ContextCompat.getDrawable(requireContext(), if (i <= unlocked) R.drawable.lesson_enabled_gradient else R.drawable.lesson_disabled_gradient)
            btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            btn.isEnabled = i <= unlocked
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 0, 16)
            btn.layoutParams = params
            if (i <= unlocked) {
                btn.setOnClickListener {
                    val learningFragment = LearningFragment.newInstance(i - 1)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, learningFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
            lessonListContainer.addView(btn)
        }
    }
    
    private fun setupClickListeners(view: View, totalLesson: Int) {
        val dailyQuestCard = view.findViewById<MaterialCardView>(R.id.daily_quest_card)
        dailyQuestCard?.setOnClickListener {
            val questFragment = QuestFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, questFragment)
                .addToBackStack(null)
                .commit()
        }
        val leaderboardCard = view.findViewById<MaterialCardView>(R.id.leaderboard_card)
        leaderboardCard?.setOnClickListener {
            val leaderboardFragment = LeaderboardFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, leaderboardFragment)
                .addToBackStack(null)
                .commit()
        }
    }
} 