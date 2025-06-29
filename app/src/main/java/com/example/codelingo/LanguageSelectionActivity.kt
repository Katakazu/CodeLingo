package com.example.codelingo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class LanguageSelectionActivity : AppCompatActivity() {

    private lateinit var cardJava: MaterialCardView
    private lateinit var cardPython: MaterialCardView
    private lateinit var cardC: MaterialCardView
    private lateinit var cardCSS: MaterialCardView
    private lateinit var cardKotlin: MaterialCardView
    private lateinit var cardPHP: MaterialCardView
    private lateinit var btnContinue: Button

    private var selectedLanguage: String? = null
    private var selectedCard: MaterialCardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_selection)

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        cardJava = findViewById(R.id.cardJava)
        cardPython = findViewById(R.id.cardPython)
        cardC = findViewById(R.id.cardC)
        cardCSS = findViewById(R.id.cardCSS)
        cardKotlin = findViewById(R.id.cardKotlin)
        cardPHP = findViewById(R.id.cardPHP)
        btnContinue = findViewById(R.id.btnContinue)
    }

    private fun setupClickListeners() {
        cardJava.setOnClickListener { selectLanguage("Java", cardJava) }
        cardPython.setOnClickListener {
            Toast.makeText(this, "Python not available yet", Toast.LENGTH_SHORT).show()
        }
        cardC.setOnClickListener {
            Toast.makeText(this, "C not available yet", Toast.LENGTH_SHORT).show()
        }
        cardCSS.setOnClickListener {
            Toast.makeText(this, "CSS not available yet", Toast.LENGTH_SHORT).show()
        }
        cardKotlin.setOnClickListener {
            Toast.makeText(this, "Kotlin not available yet", Toast.LENGTH_SHORT).show()
        }
        cardPHP.setOnClickListener {
            Toast.makeText(this, "PHP not available yet", Toast.LENGTH_SHORT).show()
        }

        btnContinue.setOnClickListener {
            proceedToNextScreen()
        }
    }

    private fun selectLanguage(language: String, card: MaterialCardView) {
        // Reset previous selection
        selectedCard?.let { resetCardSelection(it) }

        // Set new selection
        selectedLanguage = language
        selectedCard = card
        highlightSelectedCard(card)

        // Enable continue button
        enableContinueButton()
    }

    private fun resetCardSelection(card: MaterialCardView) {
        card.strokeColor = getColor(R.color.gray_light)
        card.strokeWidth = 2 // 2dp
        card.cardElevation = 4f // 4dp
    }

    private fun highlightSelectedCard(card: MaterialCardView) {
        card.strokeColor = getColor(R.color.orange_primary)
        card.strokeWidth = 4 // 4dp
        card.cardElevation = 8f // 8dp
    }

    private fun enableContinueButton() {
        btnContinue.isEnabled = true
        btnContinue.alpha = 1.0f
    }

    private fun proceedToNextScreen() {
        selectedLanguage?.let { language ->
            // Pass selected language to next activity
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("SELECTED_LANGUAGE", language)
            startActivity(intent)
            finish()
        }
    }

    // Optional: Save selection to SharedPreferences
    private fun saveLanguageSelection(language: String) {
        val sharedPref = getSharedPreferences("app_preferences", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("selected_language", language)
            apply()
        }
    }

    // Optional: Load previous selection
    private fun loadPreviousSelection() {
        val sharedPref = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val savedLanguage = sharedPref.getString("selected_language", null)

        savedLanguage?.let { language ->
            when (language) {
                "Java" -> selectLanguage("Java", cardJava)
                "Python" -> selectLanguage("Python", cardPython)
                "C" -> selectLanguage("C", cardC)
                "CSS" -> selectLanguage("CSS", cardCSS)
                "Kotlin" -> selectLanguage("Kotlin", cardKotlin)
                "PHP" -> selectLanguage("PHP", cardPHP)
            }
        }
    }
}