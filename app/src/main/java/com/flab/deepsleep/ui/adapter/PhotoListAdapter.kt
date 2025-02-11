package com.flab.deepsleep.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flab.deepsleep.R
import com.flab.deepsleep.data.entity.photo.Photo
import com.flab.deepsleep.databinding.HolderItemPhotoBinding
import com.flab.deepsleep.ui.listener.OnDeletePhotoClick

/* BookmarkFragment - Adapter */
class PhotoListAdapter(
    private val onDeletePhotoClick: OnDeletePhotoClick
) :
    ListAdapter<Photo, PhotoListAdapter.ItemViewHolder>(ITEM_DIFF_CALLBACK) {

    class ItemViewHolder(
        private val binding: HolderItemPhotoBinding,
        private val onDeletePhotoClick: OnDeletePhotoClick
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView: ImageView = binding.photoImageView
        private val btHeart: ImageView = binding.btHeart

        fun bind(photo: Photo) {
            binding.photoTitle.text = photo.description
            btHeart.isSelected = photo.isLike

            btHeart.setOnClickListener {
                onDeletePhotoClick.onDeletePhotoClick(photo)
            }

            val imageUrl = photo.urls
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            HolderItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, onDeletePhotoClick)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val ITEM_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem == newItem
            }
        }
    }

}
