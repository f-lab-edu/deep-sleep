package com.flab.deepsleep.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flab.deepsleep.R
import com.flab.deepsleep.databinding.ItemPhotoBinding
import com.flab.deepsleep.ui.listener.UiItem

class PagingAdapter(
    val onLikeToggled: (uiItem: UiItem) -> Unit
) :
    PagingDataAdapter<UiItem, PagingAdapter.ImageViewHolder>(ARTICLE_DIFF_CALLBACK) {

    class ImageViewHolder(
        private val binding: ItemPhotoBinding,
        onItemClick: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val imageView: ImageView = binding.photoImageView
        val btHeart: ImageView = binding.btHeart

        init {
            btHeart.setOnClickListener {
                onItemClick(bindingAdapterPosition)
            }
        }

        fun bind(uiItem: UiItem) {
            binding.photoTitle.text = uiItem.description
            btHeart.isSelected = uiItem.isLike

            val imageUrl = uiItem.urls
            if (imageUrl != null) {
                Glide.with(imageView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding,
            onItemClick = { position ->
                val photo = getItem(position)
                photo?.let {
                    this.onLikeToggled(photo)
                }
            }
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    /* Paging */
    companion object {
        private val ARTICLE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<UiItem>() {
            override fun areItemsTheSame(oldItem: UiItem, newItem: UiItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UiItem, newItem: UiItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}