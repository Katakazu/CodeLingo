package com.example.codelingo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.codelingo.data.model.ModuleItem
import com.example.codelingo.databinding.ItemModuleBinding

class ModuleAdapter(private val items: List<ModuleItem>) : RecyclerView.Adapter<ModuleAdapter.ModulViewHolder>() {

    inner class ModulViewHolder(private val binding: ItemModuleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(modul: ModuleItem, position: Int) {
            binding.tvNumber.text = modul.number.toString()
            binding.tvStatus.text = modul.status
            binding.progressBar.progress = modul.progress

            val layoutParams = binding.root.layoutParams as ViewGroup.MarginLayoutParams
            val density = binding.root.context.resources.displayMetrics.density
            val margin = (64 * density).toInt()

            layoutParams.marginStart = 0
            layoutParams.marginEnd = 0

            if (position % 2 == 0) {
                layoutParams.marginStart = margin
            } else {
                layoutParams.marginEnd = margin
            }

            binding.root.layoutParams = layoutParams
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModulViewHolder {
        val binding = ItemModuleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ModulViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ModulViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size
}