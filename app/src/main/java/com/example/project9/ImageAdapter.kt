package com.example.project9

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project9.databinding.ImageItemBinding
import com.example.project9.Image

/**
 * RecyclerView Adapter for displaying a list of images using the ListAdapter.
 * This adapter is responsible for inflating the layout for each item in the RecyclerView,
 * binding data to the corresponding views, and handling item click events.
 */
class ImageAdapter(val context: Context,val onImageClicked:(Image)->Unit)
    : ListAdapter<Image, ImageAdapter.ImageItemViewHolder>(ImageDiffItemCallback()) {
    /**
     * Inflates the layout for a new RecyclerView item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ImageItemViewHolder = ImageItemViewHolder.inflateFrom(parent)
    /**
     * Binds the data to the views of a RecyclerView item at the given position.
     */
    override fun onBindViewHolder(holder: ImageItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, context)
        holder.binding.image.setOnClickListener {
            onImageClicked(getItem(holder.adapterPosition))
        }
    }
    /**
     * ViewHolder class representing an item view in the RecyclerView.
     */
    class ImageItemViewHolder(val binding: ImageItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        companion object {
            /**
             * Inflates the ViewHolder from the provided parent view group.
             */
            fun inflateFrom(parent: ViewGroup): ImageItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ImageItemBinding.inflate(layoutInflater, parent, false)
                return ImageItemViewHolder(binding)
            }
        }
        /**
         * Binds the data of an Image to the views in the item layout.
         */
        fun bind(image: Image, context: Context) {
            Glide.with(context).load(image.imageUrl).into(binding.image)
        }
    }
}