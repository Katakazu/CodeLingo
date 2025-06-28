package com.example.codelingo.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.codelingo.R
import com.example.codelingo.ui.auth.LoginActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var userPreferences: UserPreferences
    private val splashDelayMs = 2000L // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        userPreferences = UserPreferences(this)

        // Hide action bar for splash screen
        supportActionBar?.hide()

        // Delay and navigate to appropriate screen
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, splashDelayMs)
    }

    private fun navigateToNextScreen() {
        val intent = if (userPreferences.isUserLoggedIn()) {
            // User is already logged in, go to main activity
            Intent(this, MainActivity::class.java)
        } else {
            // User needs to login/register
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish() // Remove splash from back stack
    }
}