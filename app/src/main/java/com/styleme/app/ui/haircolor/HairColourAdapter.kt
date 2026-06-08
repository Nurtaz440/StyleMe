package com.styleme.app.ui.haircolor

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.styleme.app.databinding.ItemHairColourBinding
import com.styleme.app.models.HairColour

class HairColourAdapter(
    private val onColourClick: (HairColour) -> Unit
) : ListAdapter<HairColour, HairColourAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemHairColourBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(colour: HairColour) {
            // Fix: show formatted name only, not hash
            val displayName = (colour.colourName ?: colour.colourHash)
                .replace('_', ' ')
                .split(' ')
                .joinToString("\n") { word ->
                    word.replaceFirstChar { it.uppercase() }
                }
            binding.tvColourName.text = displayName

            // Fix colour swatch
            try {
                val hex = colour.colourHash.let {
                    if (it.startsWith("#")) it else "#$it"
                }
                binding.vColourSwatch.setBackgroundColor(
                    android.graphics.Color.parseColor(hex)
                )
            } catch (e: Exception) {
                binding.vColourSwatch.setBackgroundColor(android.graphics.Color.GRAY)
            }

            binding.root.setOnClickListener { onColourClick(colour) }
        }

        private fun formatColourName(name: String) =
            name.replace('_', ' ').split(' ')
                .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHairColourBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<HairColour>() {
        override fun areItemsTheSame(a: HairColour, b: HairColour) = a.id == b.id
        override fun areContentsTheSame(a: HairColour, b: HairColour) = a == b
    }
}
