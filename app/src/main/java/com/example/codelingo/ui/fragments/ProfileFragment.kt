package com.example.codelingo.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.codelingo.LoginActivity
import com.example.codelingo.R
import com.example.codelingo.data.preferences.UserPreferences
import com.example.codelingo.viewmodel.AuthViewModel
import com.google.android.material.card.MaterialCardView

class ProfileFragment : Fragment() {

    private lateinit var userPreferences: UserPreferences
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())
        authViewModel = AuthViewModel(requireActivity().application)
        
        setupProfileData(view)
        setupClickListeners(view)
        setupBadges(view)
    }

    private fun setupProfileData(view: View) {
        // Nama & subtext
        val username = userPreferences.getUsername()
        val level = userPreferences.getUserLevel()
        val language = userPreferences.getSelectedLanguage()
        
        view.findViewById<TextView>(R.id.profileName).text = username
        view.findViewById<TextView>(R.id.profileSubtext).text = "Level $level • $language"
        
        // Statistik dengan animasi
        val totalXp = userPreferences.getTotalXp()
        val streak = userPreferences.getUserDays()
        val lessonCompleted = (userPreferences.getCurrentLesson() - 1).coerceAtLeast(0)
        
        animateNumber(view.findViewById(R.id.profileXp), totalXp)
        animateNumber(view.findViewById(R.id.profileStreak), streak)
        animateNumber(view.findViewById(R.id.profileLesson), lessonCompleted)
        
        // Bahasa
        view.findViewById<TextView>(R.id.profileLanguage).text = language
        updateLanguageIcon(view.findViewById(R.id.profileLanguageIcon), language)
        
        // Versi aplikasi
        view.findViewById<TextView>(R.id.profileVersion).text = "v1.0.0"
        
        // Switch notifikasi
        val notifSwitch = view.findViewById<Switch>(R.id.profileNotificationSwitch)
        notifSwitch.isChecked = userPreferences.isNotificationEnabled()
        notifSwitch.setOnCheckedChangeListener { _, isChecked ->
            userPreferences.setNotificationEnabled(isChecked)
        }
    }

    private fun setupClickListeners(view: View) {
        // Edit avatar/profile
        view.findViewById<ImageView>(R.id.btnEditAvatar).setOnClickListener {
            showEditProfileDialog()
        }
        
        // Bahasa pemrograman
        view.findViewById<MaterialCardView>(R.id.profileLanguageCard).setOnClickListener {
            showLanguageSelectionDialog()
        }
        
        // Tentang CodeLingo
        view.findViewById<MaterialCardView>(R.id.profileAboutCard).setOnClickListener {
            showAboutDialog()
        }
        
        // Logout
        view.findViewById<MaterialCardView>(R.id.profileLogoutCard).setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun setupBadges(view: View) {
        val badgeRow = view.findViewById<LinearLayout>(R.id.badgeRow)
        badgeRow.removeAllViews()
        
        // Dummy badges berdasarkan level dan achievement
        val level = userPreferences.getUserLevel()
        val totalXp = userPreferences.getTotalXp()
        
        if (level >= 1) {
            addBadge(badgeRow, R.drawable.ic_trophy)
        }
        if (totalXp >= 500) {
            addBadge(badgeRow, R.drawable.ic_fire)
        }
        if (userPreferences.getUserDays() >= 7) {
            addBadge(badgeRow, R.drawable.ic_calendar)
        }
    }

    private fun addBadge(container: LinearLayout, iconRes: Int) {
        val badge = ImageView(requireContext())
        badge.setImageResource(iconRes)
        badge.layoutParams = LinearLayout.LayoutParams(48, 48).apply {
            marginEnd = 8
        }
        badge.setColorFilter(ContextCompat.getColor(requireContext(), R.color.orange_primary))
        container.addView(badge)
    }

    private fun animateNumber(textView: TextView, targetValue: Int) {
        textView.text = "0"
        val animator = android.animation.ValueAnimator.ofInt(0, targetValue)
        animator.duration = 1000
        animator.addUpdateListener { animation ->
            textView.text = animation.animatedValue.toString()
        }
        animator.start()
    }

    private fun updateLanguageIcon(imageView: ImageView, language: String) {
        val iconRes = when (language) {
            "Java" -> R.drawable.ic_java
            "Python" -> R.drawable.ic_python
            "C" -> R.drawable.ic_c
            "CSS" -> R.drawable.ic_css
            "Kotlin" -> R.drawable.ic_kotlin
            "PHP" -> R.drawable.ic_php
            else -> R.drawable.ic_java
        }
        imageView.setImageResource(iconRes)
    }

    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.editTextName)
        nameEditText.setText(userPreferences.getUsername())
        
        AlertDialog.Builder(requireContext())
            .setTitle("Edit Profile")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val newName = nameEditText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    userPreferences.setUsername(newName)
                    view?.findViewById<TextView>(R.id.profileName)?.text = newName
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf("Java", "Python", "C", "CSS", "Kotlin", "PHP")
        val currentLanguage = userPreferences.getSelectedLanguage()
        val currentIndex = languages.indexOf(currentLanguage).coerceAtLeast(0)
        
        AlertDialog.Builder(requireContext())
            .setTitle("Pilih Bahasa Pemrograman")
            .setSingleChoiceItems(languages, currentIndex) { dialog, which ->
                val selectedLanguage = languages[which]
                if (selectedLanguage == "Java") {
                    userPreferences.setSelectedLanguage(selectedLanguage)
                    view?.findViewById<TextView>(R.id.profileLanguage)?.text = selectedLanguage
                    view?.findViewById<TextView>(R.id.profileSubtext)?.text = "Level ${userPreferences.getUserLevel()} • $selectedLanguage"
                    updateLanguageIcon(view?.findViewById(R.id.profileLanguageIcon) ?: return@setSingleChoiceItems, selectedLanguage)
                    dialog.dismiss()
                } else {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Info")
                        .setMessage("Bahasa $selectedLanguage masih dalam pengembangan.")
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Tentang CodeLingo")
            .setMessage("CodeLingo adalah aplikasi pembelajaran pemrograman yang menyenangkan dan interaktif.\n\n" +
                    "Versi: 1.0.0\n" +
                    "Developer: CodeLingo Team\n\n" +
                    "Fitur:\n" +
                    "• Belajar pemrograman dengan cara yang menyenangkan\n" +
                    "• Sistem quest harian\n" +
                    "• Leaderboard kompetitif\n" +
                    "• Progress tracking yang detail")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Keluar")
            .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
            .setPositiveButton("Ya") { _, _ ->
                authViewModel.logout()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("Tidak", null)
            .show()
    }
} 