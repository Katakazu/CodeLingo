package com.example.codelingo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.codelingo.R
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
        cardPython.setOnClickListener { selectLanguage("Python", cardPython) }
        cardC.setOnClickListener { selectLanguage("C", cardC) }
        cardCSS.setOnClickListener { selectLanguage("CSS", cardCSS) }
        cardKotlin.setOnClickListener { selectLanguage("Kotlin", cardKotlin) }
        cardPHP.setOnClickListener { selectLanguage("PHP", cardPHP) }

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
        card.strokeWidth = resources.getDimensionPixelSize(R.dimen.card_stroke_width_normal)
        card.cardElevation = resources.getDimension(R.dimen.card_elevation_normal)
    }

    private fun highlightSelectedCard(card: MaterialCardView) {
        card.strokeColor = getColor(R.color.orange_primary)
        card.strokeWidth = resources.getDimensionPixelSize(R.dimen.card_stroke_width_selected)
        card.cardElevation = resources.getDimension(R.dimen.card_elevation_selected)
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

    override fun onBackPressed() {
        super.onBackPressed()
        // Navigate back to previous screen or close app
        finish()
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