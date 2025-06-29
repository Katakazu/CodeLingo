package com.example.codelingo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.codelingo.databinding.ActivityMainBinding
import com.example.codelingo.ui.fragments.HomeFragment
import com.example.codelingo.ui.fragments.LeaderboardFragment
import com.example.codelingo.ui.fragments.QuestFragment
import com.example.codelingo.ui.fragments.ProfileFragment
import com.example.codelingo.viewmodel.AuthViewModel
import com.google.firebase.FirebaseApp
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        Log.d("FirebaseTest", "Firebase initialized: ${FirebaseApp.getApps(this).isNotEmpty()}")
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        authViewModel.setUserPreferences(com.example.codelingo.data.preferences.UserPreferences(this))

        // Check if user is logged in
        if (!authViewModel.isUserLoggedIn()) {
            // Redirect to login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Refresh user data from Firestore to ensure we have the latest username
        authViewModel.refreshUserData()

        // Hide action bar
        supportActionBar?.hide()

        // Setup bottom navigation
        setupBottomNavigation()

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_leaderboard -> {
                    loadFragment(LeaderboardFragment())
                    true
                }
                R.id.nav_quest -> {
                    loadFragment(QuestFragment())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}