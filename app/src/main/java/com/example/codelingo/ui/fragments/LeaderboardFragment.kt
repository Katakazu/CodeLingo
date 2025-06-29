package com.example.codelingo.ui.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.codelingo.R
import com.example.codelingo.data.preferences.UserPreferences
import com.example.codelingo.viewmodel.LeaderboardViewModel
import com.example.codelingo.data.model.AppUser
import androidx.lifecycle.ViewModelProvider

class LeaderboardFragment : Fragment() {

    private lateinit var leaderboardViewModel: LeaderboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set placeholder to avoid showing hardcoded names
        view.findViewById<TextView>(R.id.top1Name)?.text = "-"
        view.findViewById<TextView>(R.id.top1Xp)?.text = "-"
        view.findViewById<TextView>(R.id.top2Name)?.text = "-"
        view.findViewById<TextView>(R.id.top2Xp)?.text = "-"
        view.findViewById<TextView>(R.id.top3Name)?.text = "-"
        view.findViewById<TextView>(R.id.top3Xp)?.text = "-"

        leaderboardViewModel = ViewModelProvider(this)[LeaderboardViewModel::class.java]
        leaderboardViewModel.leaderboardData.observe(viewLifecycleOwner) { users ->
            showLeaderboard(view, users)
            showTop3(view, users)
        }
        leaderboardViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // TODO: tampilkan/hide loading indicator jika perlu
        }
        leaderboardViewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null) {
                // TODO: tampilkan error ke user jika perlu
            }
        }
    }

    private fun showLeaderboard(view: View, users: List<AppUser>) {
        val prefs = UserPreferences(requireContext())
        val currentUserUid = prefs.getCurrentUserId()
        val sorted = users.sortedByDescending { it.totalScore }
        val top10 = sorted.take(10)
        val userIndex = sorted.indexOfFirst { it.uid == currentUserUid }
        val userInTop10 = userIndex in 0..9

        val numberFormat = java.text.NumberFormat.getInstance(java.util.Locale.US)
        val rankingList = view.findViewById<LinearLayout>(R.id.rankingListContainer)
        rankingList?.removeAllViews()
        for (i in 3 until top10.size) {
            val user = top10[i]
            val row = layoutInflater.inflate(R.layout.item_leaderboard_row, rankingList, false)
            val rankText = row.findViewById<TextView>(R.id.rankText)
            val nameText = row.findViewById<TextView>(R.id.nameText)
            val xpText = row.findViewById<TextView>(R.id.xpText)
            val avatarText = row.findViewById<TextView>(R.id.avatarText)
            rankText.text = (i + 1).toString()
            nameText.text = user.username
            xpText.text = "${numberFormat.format(user.totalScore)} XP"
            avatarText.text = getInitials(user.username)
            when (i + 1) {
                1 -> rankText.text = "🥇"
                2 -> rankText.text = "🥈"
                3 -> rankText.text = "🥉"
            }
            if (user.uid == currentUserUid) {
                (row as com.google.android.material.card.MaterialCardView).setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange_light))
                nameText.setTypeface(null, Typeface.BOLD)
            }
            rankingList.addView(row)
        }
        val userRowContainer = view.findViewById<LinearLayout>(R.id.userRankContainer)
        userRowContainer?.removeAllViews()
        if (!userInTop10 && userIndex != -1) {
            val user = sorted[userIndex]
            val userRow = layoutInflater.inflate(R.layout.item_leaderboard_row, userRowContainer, false)
            val rankText = userRow.findViewById<TextView>(R.id.rankText)
            val nameText = userRow.findViewById<TextView>(R.id.nameText)
            val xpText = userRow.findViewById<TextView>(R.id.xpText)
            val avatarText = userRow.findViewById<TextView>(R.id.avatarText)
            rankText.text = (userIndex + 1).toString()
            nameText.text = user.username
            xpText.text = "${numberFormat.format(user.totalScore)} XP"
            avatarText.text = getInitials(user.username)
            (userRow as com.google.android.material.card.MaterialCardView).setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange_light))
            nameText.setTypeface(null, Typeface.BOLD)
            userRowContainer.addView(userRow)
        }
    }

    private fun showTop3(view: View, users: List<AppUser>) {
        val sorted = users.sortedByDescending { it.totalScore }
        val top3 = sorted.take(3)
        // 1st place
        val firstName = view.findViewById<TextView>(R.id.top1Name)
        val firstXp = view.findViewById<TextView>(R.id.top1Xp)
        if (top3.size > 0) {
            firstName.text = top3[0].username
            firstXp.text = "${top3[0].totalScore} XP"
        } else {
            firstName.text = "-"
            firstXp.text = "-"
        }
        // 2nd place
        val secondName = view.findViewById<TextView>(R.id.top2Name)
        val secondXp = view.findViewById<TextView>(R.id.top2Xp)
        if (top3.size > 1) {
            secondName.text = top3[1].username
            secondXp.text = "${top3[1].totalScore} XP"
        } else {
            secondName.text = "-"
            secondXp.text = "-"
        }
        // 3rd place
        val thirdName = view.findViewById<TextView>(R.id.top3Name)
        val thirdXp = view.findViewById<TextView>(R.id.top3Xp)
        if (top3.size > 2) {
            thirdName.text = top3[2].username
            thirdXp.text = "${top3[2].totalScore} XP"
        } else {
            thirdName.text = "-"
            thirdXp.text = "-"
        }
    }

    // Helper to get initials from username
    private fun getInitials(name: String?): String {
        if (name.isNullOrBlank()) return "-"
        val parts = name.trim().split(" ")
        return if (parts.size >= 2) (parts[0].firstOrNull()?.toString() ?: "") + (parts[1].firstOrNull()?.toString() ?: "")
        else name.take(2).uppercase()
    }
} 