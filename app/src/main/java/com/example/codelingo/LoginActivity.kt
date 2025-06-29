package com.example.codelingo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.codelingo.databinding.ActivityLoginBinding
import com.example.codelingo.data.preferences.UserPreferences
import com.example.codelingo.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = UserPreferences(this)
        if (prefs.isUserLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        authViewModel.setUserPreferences(prefs)

        // Observe login result
        authViewModel.loginResult.observe(this) { result ->
            when (result) {
                is AuthViewModel.AuthResult.Loading -> {
                    binding.progressBar.visibility = if (result.isLoading) View.VISIBLE else View.GONE
                    binding.btnMasuk.isEnabled = !result.isLoading
                }
                is AuthViewModel.AuthResult.Success -> {
                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LanguageSelectionActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is AuthViewModel.AuthResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Tombol Masuk ditekan
        binding.btnMasuk.setOnClickListener {
            val usernameOrEmail = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (usernameOrEmail.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username/email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                authViewModel.loginUser(usernameOrEmail, password)
            }
        }

        // Tombol "Lupa Password"
        binding.tvLupaPassword.setOnClickListener {
            Toast.makeText(this, "Fitur belum tersedia", Toast.LENGTH_SHORT).show()
        }

        // Tombol "Daftar"
        binding.daftar.setOnClickListener {
            Toast.makeText(this, "Menuju halaman pendaftaran...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
