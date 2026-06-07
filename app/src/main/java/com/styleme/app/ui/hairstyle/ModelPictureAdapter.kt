package com.styleme.app.ui.hairstyle

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.styleme.app.databinding.ItemModelPictureBinding
import com.styleme.app.models.ModelPicture

class ModelPictureAdapter(
    private val onImageNeeded: (Int, (Bitmap?) -> Unit) -> Unit,
    private val onModelClick: (ModelPicture) -> Unit
) : ListAdapter<ModelPicture, ModelPictureAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemModelPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ModelPicture) {
            // Show file name as label
            binding.tvModelName.text = model.fileName
                .substringBeforeLast('.')
                .replace('_', ' ')
                .replaceFirstChar { it.uppercase() }

            // Show coloured placeholder when no image available
            if (model.filePath != null) {
                com.bumptech.glide.Glide.with(binding.root.context)
                    .load(model.filePath)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(binding.ivModelPicture)
            } else {
                // Show style colour placeholder
                val colours = listOf(
                    0xFF7B4FBE, 0xFFE75480, 0xFFFF8C42,
                    0xFF1A73E8, 0xFF009688, 0xFF8E44AD,
                    0xFFE74C3C, 0xFF2ECC71, 0xFFF39C12,
                    0xFF1ABC9C
                )
                val color = colours[(model.id - 1) % colours.size].toInt()
                binding.ivModelPicture.setBackgroundColor(color)
                binding.ivModelPicture.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            binding.root.setOnClickListener { onModelClick(model) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemModelPictureBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<ModelPicture>() {
        override fun areItemsTheSame(a: ModelPicture, b: ModelPicture) = a.id == b.id
        override fun areContentsTheSame(a: ModelPicture, b: ModelPicture) = a == b
    }
}
