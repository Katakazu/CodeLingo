package com.example.codelingo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.codelingo.R
import com.example.codelingo.databinding.ItemLanguageBinding
import com.example.codelingo.data.model.ProgrammingLanguage

class LanguageAdapter(
    private val languages: List<ProgrammingLanguage>,
    private val onLanguageClick: (ProgrammingLanguage) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = ItemLanguageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LanguageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(languages[position])
    }

    override fun getItemCount(): Int = languages.size

    inner class LanguageViewHolder(
        private val binding: ItemLanguageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(language: ProgrammingLanguage) {
            binding.apply {
                tvLanguageName.text = language.name

                // Set icon based on language name
                val iconRes =
                    when (language.name.lowercase()) {
                    "java" -> R.drawable.ic_java
//                    "python" -> R.drawable.ic_python
//                    "c" -> R.drawable.ic_c
//                    "css" -> R.drawable.ic_css
//                    "kotlin" -> R.drawable.ic_kotlin
//                    "php" -> R.drawable.ic_php
                      else -> R.drawable.ic_coding
                    }
                ivLanguageIcon.setImageResource(iconRes)

                // Update selection state
                updateSelectionState(language.isSelected)

                // Set click listener
                root.setOnClickListener {
                    onLanguageClick(language)
                }
            }
        }

        private fun updateSelectionState(isSelected: Boolean) {
            binding.apply {
                if (isSelected) {
                    cardLanguage.setCardBackgroundColor(
                        ContextCompat.getColor(root.context, R.color.orange_light)
                    )
                    cardLanguage.strokeColor =
                        ContextCompat.getColor(root.context, R.color.orange)
                    cardLanguage.strokeWidth = 4
                } else {
                    cardLanguage.setCardBackgroundColor(
                        ContextCompat.getColor(root.context, R.color.white)
                    )
                    cardLanguage.strokeColor =
                        ContextCompat.getColor(root.context, R.color.gray_light)
                    cardLanguage.strokeWidth = 2
                }
            }
        }
    }
}