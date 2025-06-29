package com.example.codelingo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.codelingo.data.model.LeaderboardItem
import com.example.codelingo.databinding.ItemLeaderboardBinding

class LeaderboardAdapter(private val list: List<LeaderboardItem>) :
    RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {
    inner class LeaderboardViewHolder(private val binding: ItemLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: LeaderboardItem) {
            binding.tvRank.text = "${item.rank}."
            binding.tvName.text = item.name
            binding.tvLevel.text = "Level ${item.level}"

            when (item.rank) {
                1 -> binding.ivBadge.setImageResource(R.drawable.ic_gold_medal)
                2 -> binding.ivBadge.setImageResource(R.drawable.ic_silver_medal)
                3 -> binding.ivBadge.setImageResource(R.drawable.ic_bronze_medal)
                else -> {
                    binding.ivBadge.visibility = View.GONE
                }
            }

            // Untuk rank user terakhir
            if (item.name == "Kamu") {
                binding.root.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.orange_light)
                )
            } else {
                binding.root.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.white)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val binding = ItemLeaderboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaderboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}