package com.example.codelingo.ui.language

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.codelingo.databinding.ActivityLanguageSelectionBinding
import com.example.codelingo.data.model.ProgrammingLanguage

class LanguageSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageSelectionBinding
    private lateinit var languageAdapter: LanguageAdapter
    private val programmingLanguages = mutableListOf<ProgrammingLanguage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide action bar
        supportActionBar?.hide()

        setupLanguages()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupLanguages() {
        programmingLanguages.addAll(listOf(
            ProgrammingLanguage(1, "Java", "ic_java", false),
            ProgrammingLanguage(2, "Python", "ic_python", false),
            ProgrammingLanguage(3, "C", "ic_c", false),
            ProgrammingLanguage(4, "CSS", "ic_css", false),
            ProgrammingLanguage(5, "Kotlin", "ic_kotlin", false),
            ProgrammingLanguage(6, "PHP", "ic_php", false)
        ))
    }

    private fun setupRecyclerView() {
        languageAdapter = LanguageAdapter(programmingLanguages) { language ->
            handleLanguageSelection(language)
        }

        binding.rvLanguages.apply {
            layoutManager = GridLayoutManager(this@LanguageSelectionActivity, 2)
            adapter = languageAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnLanjutkan.setOnClickListener {
            val selectedLanguages = programmingLanguages.filter { it.isSelected }
            if (selectedLanguages.isNotEmpty()) {
                saveSelectedLanguagesAndProceed(selectedLanguages)
            } else {
                // Show error message
                showError("Pilih minimal satu bahasa pemrograman")
            }
        }
    }

    private fun handleLanguageSelection(language: ProgrammingLanguage) {
        val index = programmingLanguages.indexOf(language)
        if (index != -1) {
            programmingLanguages[index].isSelected = !programmingLanguages[index].isSelected
            languageAdapter.notifyItemChanged(index)

            // Enable/disable continue button based on selection
            updateContinueButton()
        }
    }

    private fun updateContinueButton() {
        val hasSelection = programmingLanguages.any { it.isSelected }
        binding.btnLanjutkan.isEnabled = hasSelection
        binding.btnLanjutkan.alpha = if (hasSelection) 1.0f else 0.5f
    }

    private fun saveSelectedLanguagesAndProceed(selectedLanguages: List<ProgrammingLanguage>) {
        // Save selected languages to preferences or database
        // For now, just proceed to main activity
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("selected_languages", ArrayList(selectedLanguages.map { it.name }))
        startActivity(intent)
        finish()
    }

    private fun showError(message: String) {
        // You can use Snackbar or Toast here
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}