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
        // Setup click listeners
        setupClickListeners(view)
    }
    
    private fun showUserStats(view: View) {
        val prefs = UserPreferences(requireContext())
        val textLevel = view.findViewById<TextView>(R.id.textLevel)
        val textXp = view.findViewById<TextView>(R.id.textXp)
        val textDays = view.findViewById<TextView>(R.id.textDays)
        val level = prefs.getUserLevel()
        val xp = prefs.getUserXp()
        val targetXp = 200
        textLevel.text = level.toString()
        textXp.text = "$xp/$targetXp"
        textDays.text = prefs.getUserDays().toString()
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