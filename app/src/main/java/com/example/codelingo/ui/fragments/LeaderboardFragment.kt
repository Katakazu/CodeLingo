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
import java.text.NumberFormat
import java.util.Locale

class LeaderboardFragment : Fragment() {

    data class User(val name: String, val xp: Int)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLeaderboard(view)
    }

    private fun showLeaderboard(view: View) {
        // Hardcode user list (nama, XP)
        val prefs = UserPreferences(requireContext())
        val currentUserName = "Admin User" // Ganti sesuai user login jika ada
        val currentUserXp = prefs.getTotalXp()
        val users = mutableListOf(
            User("Alex", 3200),
            User("Sarah", 2450),
            User("Mike", 1890),
            User("Emma", 1650),
            User("David", 1420),
            User("Lisa", 1200),
            User("John", 1100),
            User("Anna", 950),
            User("Tom", 800),
            User("Nina", 700),
            User("Kevin", 600)
        )
        // Pastikan user login hanya satu kali di list
        users.removeAll { it.name == currentUserName }
        users.add(User(currentUserName, currentUserXp))
        // Urutkan dan ranking
        val sorted = users.sortedByDescending { it.xp }
        val top10 = sorted.take(10)
        val userRank = sorted.indexOfFirst { it.name == currentUserName } + 1
        val userInTop10 = userRank in 1..10

        val numberFormat = NumberFormat.getInstance(Locale.US)
        // Tampilkan top 10 di layout (mulai dari rank 4 ke bawah, karena 1-3 sudah di layout XML)
        val rankingList = view.findViewById<LinearLayout>(R.id.rankingListContainer)
        rankingList?.removeAllViews()
        for (i in 3 until top10.size) {
            val user = top10[i]
            val row = layoutInflater.inflate(R.layout.item_leaderboard_row, rankingList, false)
            val rankText = row.findViewById<TextView>(R.id.rankText)
            val nameText = row.findViewById<TextView>(R.id.nameText)
            val xpText = row.findViewById<TextView>(R.id.xpText)
            rankText.text = (i + 1).toString()
            nameText.text = user.name
            xpText.text = "${numberFormat.format(user.xp)} XP"
            // Emoji medali untuk ranking 1-3
            when (i + 1) {
                1 -> rankText.text = "🥇"
                2 -> rankText.text = "🥈"
                3 -> rankText.text = "🥉"
            }
            // Highlight user login
            if (user.name == currentUserName) {
                (row as com.google.android.material.card.MaterialCardView).setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange_light))
                nameText.setTypeface(null, Typeface.BOLD)
            }
            rankingList.addView(row)
        }
        // Jika user login tidak masuk top 10, tampilkan di bawah
        val userRowContainer = view.findViewById<LinearLayout>(R.id.userRankContainer)
        userRowContainer?.removeAllViews()
        if (!userInTop10) {
            val userRow = layoutInflater.inflate(R.layout.item_leaderboard_row, userRowContainer, false)
            val rankText = userRow.findViewById<TextView>(R.id.rankText)
            val nameText = userRow.findViewById<TextView>(R.id.nameText)
            val xpText = userRow.findViewById<TextView>(R.id.xpText)
            rankText.text = userRank.toString()
            nameText.text = currentUserName
            xpText.text = "${numberFormat.format(currentUserXp)} XP"
            (userRow as com.google.android.material.card.MaterialCardView).setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange_light))
            nameText.setTypeface(null, Typeface.BOLD)
            userRowContainer.addView(userRow)
        }
    }
} 