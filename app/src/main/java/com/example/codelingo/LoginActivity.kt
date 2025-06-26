package com.example.codelingo.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.codelingo.R
import com.example.codelingo.databinding.ActivityLoginBinding
import com.example.codelingo.ui.language.LanguageSelectionActivity
import com.example.codelingo.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide action bar
        supportActionBar?.hide()

        // Initialize ViewModel
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnMasuk.setOnClickListener {
            handleLogin()
        }

        binding.btnGoogle.setOnClickListener {
            handleGoogleLogin()
        }

        binding.btnFacebook.setOnClickListener {
            handleFacebookLogin()
        }

        binding.tvSudahPunya.setOnClickListener {
            navigateToRegister()
        }

        binding.tvLupaPassword.setOnClickListener {
            handleForgotPassword()
        }
    }

    private fun handleLogin() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (validateInput(username, password)) {
            authViewModel.loginUser(username, password)
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            binding.etUsername.error = "Username tidak boleh kosong"
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Password tidak boleh kosong"
            return false
        }

        if (password.length < 6) {
            binding.etPassword.error = "Password minimal 6 karakter"
            return false
        }

        return true
    }

    private fun observeViewModel() {
        authViewModel.loginResult.observe(this) { result ->
            when (result) {
                is AuthViewModel.AuthResult.Success -> {
                    if (result.isFirstTime) {
                        navigateToLanguageSelection()
                    } else {
                        navigateToMain()
                    }
                }
                is AuthViewModel.AuthResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
                is AuthViewModel.AuthResult.Loading -> {
                    // Show loading indicator
                    binding.btnMasuk.isEnabled = !result.isLoading
                }
            }
        }
    }

    private fun handleGoogleLogin() {
        // Implement Google Sign-In
        Toast.makeText(this, "Google login will be implemented", Toast.LENGTH_SHORT).show()
    }

    private fun handleFacebookLogin() {
        // Implement Facebook Sign-In
        Toast.makeText(this, "Facebook login will be implemented", Toast.LENGTH_SHORT).show()
    }

    private fun handleForgotPassword() {
        // Navigate to forgot password screen
        Toast.makeText(this, "Forgot password feature will be implemented", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLanguageSelection() {
        val intent = Intent(this, LanguageSelectionActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}