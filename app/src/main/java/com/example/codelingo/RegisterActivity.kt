package com.example.codelingo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.codelingo.databinding.ActivityRegisterBinding
import com.example.codelingo.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Halaman Daftar"

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        authViewModel.setUserPreferences(com.example.codelingo.data.preferences.UserPreferences(this))

        // Observe register result
        authViewModel.registerResult.observe(this) { result ->
            when (result) {
                is AuthViewModel.AuthResult.Loading -> {
                    binding.progressBar.visibility = if (result.isLoading) View.VISIBLE else View.GONE
                    binding.btnDaftar.isEnabled = !result.isLoading
                }
                is AuthViewModel.AuthResult.Success -> {
                    Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LanguageSelectionActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is AuthViewModel.AuthResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Tombol Daftar
        binding.btnDaftar.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            when {
                username.isEmpty() -> {
                    Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
                email.isEmpty() -> {
                    Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
                password != confirmPassword -> {
                    Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                }
                password.length < 6 -> {
                    Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    authViewModel.registerUser(username, email, password)
                }
            }
        }

        // Tombol "Masuk"
        binding.masuk.setOnClickListener {
            Toast.makeText(this, "Menuju halaman masuk...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}