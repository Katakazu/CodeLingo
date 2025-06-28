package com.example.yourapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.widget.Button
import android.widget.TextView
import com.example.codelingo.R
import com.example.codelingo.ui.auth.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnRegister: Button
    private lateinit var btnGoogle: Button
    private lateinit var btnFacebook: Button
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        etEmail = findViewById(R.id.etEmail)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnGoogle = findViewById(R.id.btnGoogle)
        btnFacebook = findViewById(R.id.btnFacebook)
        tvLogin = findViewById(R.id.tvLogin)
    }

    private fun setupClickListeners() {
        btnRegister.setOnClickListener {
            performRegister()
        }

        btnGoogle.setOnClickListener {
            performGoogleSignUp()
        }

        btnFacebook.setOnClickListener {
            performFacebookSignUp()
        }

        tvLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun performRegister() {
        val email = etEmail.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        // Validate inputs
        if (!validateInputs(email, username, password, confirmPassword)) {
            return
        }

        // Show loading state
        btnRegister.isEnabled = false
        btnRegister.text = "Mendaftar..."

        // TODO: Implement actual registration logic here
        // This could be API call, Firebase Auth, etc.
        simulateRegistration(email, username, password)
    }

    private fun validateInputs(email: String, username: String, password: String, confirmPassword: String): Boolean {
        // Validate email
        if (TextUtils.isEmpty(email)) {
            etEmail.error = "Email tidak boleh kosong"
            etEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Format email tidak valid"
            etEmail.requestFocus()
            return false
        }

        // Validate username
        if (TextUtils.isEmpty(username)) {
            etUsername.error = "Username tidak boleh kosong"
            etUsername.requestFocus()
            return false
        }

        if (username.length < 3) {
            etUsername.error = "Username minimal 3 karakter"
            etUsername.requestFocus()
            return false
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            etPassword.error = "Password tidak boleh kosong"
            etPassword.requestFocus()
            return false
        }

        if (password.length < 6) {
            etPassword.error = "Password minimal 6 karakter"
            etPassword.requestFocus()
            return false
        }

        // Validate confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.error = "Konfirmasi password tidak boleh kosong"
            etConfirmPassword.requestFocus()
            return false
        }

        if (password != confirmPassword) {
            etConfirmPassword.error = "Password tidak sama"
            etConfirmPassword.requestFocus()
            return false
        }

        return true
    }

    private fun simulateRegistration(email: String, username: String, password: String) {
        // Simulate network delay
        Thread {
            Thread.sleep(2000)
            runOnUiThread {
                // Reset button state
                btnRegister.isEnabled = true
                btnRegister.text = "Daftar"

                // Show success message
                Toast.makeText(this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show()

                // Navigate to login or main activity
                navigateToLogin()
            }
        }.start()
    }

    private fun performGoogleSignUp() {
        // TODO: Implement Google Sign-Up
        // This would typically involve Google Sign-In SDK
        Toast.makeText(this, "Google Sign-Up akan diimplementasikan", Toast.LENGTH_SHORT).show()
    }

    private fun performFacebookSignUp() {
        // TODO: Implement Facebook Sign-Up
        // This would typically involve Facebook Login SDK
        Toast.makeText(this, "Facebook Sign-Up akan diimplementasikan", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close register activity
    }

    private fun clearErrors() {
        etEmail.error = null
        etUsername.error = null
        etPassword.error = null
        etConfirmPassword.error = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navigateToLogin()
    }
}