package com.example.codelingo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.codelingo.databinding.ActivityLoginBinding
import com.example.codelingo.data.preferences.UserPreferences

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

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

        // Tombol Masuk ditekan
        binding.btnMasuk.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                // Sementara hanya cek input, belum pakai autentikasi
                if (username == "admin" && password == "admin") {
                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                    prefs.setUserLoggedIn(true)
                    val intent = Intent(this, LanguageSelectionActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this, "Username atau password salah", Toast.LENGTH_SHORT).show()
                }
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
