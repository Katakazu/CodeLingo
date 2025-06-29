package com.example.codelingo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codelingo.databinding.ActivityDashboardBinding
import com.example.codelingo.data.model.ModuleItem

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listModul = listOf(
            ModuleItem(1, "Progress", 100),
            ModuleItem(2, "Progress", 75),
            ModuleItem(3, "Progress", 0),
            ModuleItem(4, "Progress", 0),
            ModuleItem(5, "Progress", 0),
            ModuleItem(6, "Progress", 0),
            ModuleItem(7, "Progress", 0),
            ModuleItem(8, "Progress", 0),
            ModuleItem(9, "Progress", 0),
            ModuleItem(10, "Progress", 0)
        )

        val adapter = ModuleAdapter(listModul)
        binding.rvModul.layoutManager = LinearLayoutManager(this)
        binding.rvModul.adapter = adapter
        binding.rvModul.addItemDecoration(SpaceItemDecoration(16))

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Sudah di Dashboard
                    true
                }
                R.id.nav_leaderboard -> {
                    // Navigasi ke TrophyActivity
                    startActivity(Intent(this, LeaderboardActivity::class.java))
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
    }
}