package com.example.codelingo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.codelingo.databinding.ActivityOnboardingBinding
import com.example.codelingo.data.model.OnboardingItem
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity: AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOnboardingItems()

        binding.btnNext.setOnClickListener {
            if (binding.viewPager.currentItem + 1 < onboardingAdapter.itemCount) {
                binding.viewPager.currentItem += 1
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        binding.tvSkip.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setupOnboardingItems() {
        val items = listOf(
            OnboardingItem(
                "Belajar dengan Mudah",
                "Pelajari dasar-dasar pemrograman dengan cara yang menyenangkan dan interaktif.",
                R.drawable.ic_school
            ),
            OnboardingItem(
                "Permainan Edukatif",
                "Latih logika dengan tantangan coding dalam bentuk game.",
                R.drawable.ic_game
            ),
            OnboardingItem(
                "Komunitas Belajar",
                "Terhubung dengan teman, dapatkan poin, dan naikkan peringkatmu!",
                R.drawable.ic_community
            )
        )
        onboardingAdapter = OnboardingAdapter(items)
        binding.viewPager.adapter = onboardingAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }
}