package com.flab.deepsleep.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.flab.deepsleep.R
import com.flab.deepsleep.data.entity.photos.SinglePhoto
import com.flab.deepsleep.databinding.ActivityDetailsBinding
import timber.log.Timber

class DetailsActivity : AppCompatActivity() {
    val detailsBinding: ActivityDetailsBinding by lazy {
        ActivityDetailsBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailsBinding.root)

        val singlePhoto = intent.getSerializableExtra("singlePhoto") as? SinglePhoto

        singlePhoto?.let {
            loadImage(it.urls?.raw)
            bindPhotoDetails(it)
        } ?: run {
            loadImage(null)
            Timber.w("singlePhoto is null")
        }
    }

    private fun loadImage(imageUrl: String?) {
        val placeholderImage = R.drawable.ic_launcher_foreground
        Glide.with(detailsBinding.detailsImageView.context)
            .load(imageUrl ?: placeholderImage)
            .placeholder(placeholderImage)
            .into(detailsBinding.detailsImageView)
    }

    private fun bindPhotoDetails(photo: SinglePhoto) {
        detailsBinding.apply {
            detailDescription.text = photo.description ?: "No description available"
            detailCreateAt.text = photo.createdAt ?: "Unknown date"
            detailLikes.text = photo.likes.toString()
            detailUsername.text = photo.user?.username
        }
    }
}