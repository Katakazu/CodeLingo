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
        // Tampilkan statistik user
        showUserStats(view)
        // Tampilkan daftar lesson
        showLessonList(view)
        // Setup click listeners
        setupClickListeners(view)
    }
    
    private fun showUserStats(view: View) {
        val prefs = UserPreferences(requireContext())
        val textLevel = view.findViewById<TextView>(R.id.textLevel)
        val textXp = view.findViewById<TextView>(R.id.textXp)
        val textDays = view.findViewById<TextView>(R.id.textDays)
        val textLessonProgress = view.findViewById<TextView>(R.id.textLessonProgress)
        val level = prefs.getUserLevel()
        val totalXp = prefs.getTotalXp()
        // Hitung target total XP untuk naik ke level berikutnya
        val targetTotalXp = 200 * (level + 1) // 200 XP per level
        val lesson = prefs.getCurrentLesson().coerceIn(1, 10)
        textLevel.text = level.toString()
        textXp.text = "$totalXp/$targetTotalXp"
        textDays.text = prefs.getUserDays().toString()
        textLessonProgress.text = "Lesson $lesson dari 10"
    }

    private fun showLessonList(view: View) {
        val prefs = UserPreferences(requireContext())
        val lessonListContainer = view.findViewById<LinearLayout>(R.id.lessonListContainer)
        lessonListContainer.removeAllViews()
        val unlocked = prefs.getCurrentLesson().coerceIn(1, 10)
        val totalLesson = 3
        for (i in 1..totalLesson) {
            val btn = TextView(requireContext())
            btn.text = "Lesson $i"
            btn.textSize = 16f
            btn.setTypeface(null, Typeface.BOLD)
            btn.setPadding(32, 24, 32, 24)
            btn.gravity = Gravity.CENTER_VERTICAL
            btn.background = ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_background)
            btn.setTextColor(ContextCompat.getColor(requireContext(), if (i <= unlocked) R.color.text_primary else R.color.text_secondary))
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
    
    private fun setupClickListeners(view: View) {
        // Daily Quest button
        val dailyQuestCard = view.findViewById<MaterialCardView>(R.id.daily_quest_card)
        dailyQuestCard?.setOnClickListener {
            // Navigate to QuestFragment
            val questFragment = QuestFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, questFragment)
                .addToBackStack(null)
                .commit()
        }
        
        // Leaderboard button
        val leaderboardCard = view.findViewById<MaterialCardView>(R.id.leaderboard_card)
        leaderboardCard?.setOnClickListener {
            // Navigate to LeaderboardFragment
            val leaderboardFragment = LeaderboardFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, leaderboardFragment)
                .addToBackStack(null)
                .commit()
        }
        
        // Continue Learning button
        val continueLearningButton = view.findViewById<View>(R.id.continue_learning_button)
        continueLearningButton?.setOnClickListener {
            // Navigasi ke LearningFragment
            val learningFragment = LearningFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, learningFragment)
                .addToBackStack(null)
                .commit()
        }
    }
} 