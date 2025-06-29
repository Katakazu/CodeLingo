package com.example.codelingo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codelingo.DashboardActivity
import com.example.codelingo.LeaderboardAdapter
import com.example.codelingo.data.model.LeaderboardItem
import com.example.codelingo.R
import com.example.codelingo.databinding.ActivityLeaderboardBinding

class LeaderboardActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = listOf(
            LeaderboardItem(1, "Musalim Ridho", 36),
            LeaderboardItem(2, "Alex Kenko", 34),
            LeaderboardItem(3, "Rose Purple", 33),
            LeaderboardItem(4, "Arthur Jco", 30),
            LeaderboardItem(5, "Emily Watson", 25),
            LeaderboardItem(6, "Kamu", 1)
        )

        binding.bottomNav.selectedItemId = R.id.nav_leaderboard
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_leaderboard -> {
                    // Sudah di trophy
                    true
                }

                R.id.nav_home -> {
                    // Navigasi ke Dashboard
                    startActivity(Intent(this, DashboardActivity::class.java))
                    true
                }


                /*
                R.id.nav_bell -> {
                    // Navigasi ke SettingsActivity
                    startActivity(Intent(this, NotificationActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    // Navigasi ke ProfileActivity
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                } */
                else -> false
            }
        }

        val adapter = LeaderboardAdapter(data)
        binding.rvLeaderboard.layoutManager = LinearLayoutManager(this)
        binding.rvLeaderboard.adapter = adapter
    }
}