package com.example.codelingo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var logoImageView: ImageView
    private lateinit var titleTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // Ganti dengan nama layout kamu

        logoImageView = findViewById(R.id.logoImageView)
        titleTextView = findViewById(R.id.titleTextView)

        animateSplash()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
        }, 2500)
    }

    private fun animateSplash() {
        val bounceInterpolator = BounceInterpolator()

        val imageAnim = TranslateAnimation(-300f, 0f, 0f, 0f).apply {
            duration = 1200
            interpolator = bounceInterpolator
        }

        val textAnim = TranslateAnimation(300f, 0f, 0f, 0f).apply {
            duration = 1200
            interpolator = bounceInterpolator
        }

        logoImageView.startAnimation(imageAnim)
        titleTextView.startAnimation(textAnim)
    }
}