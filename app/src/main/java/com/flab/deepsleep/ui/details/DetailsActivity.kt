package com.flab.deepsleep.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.flab.deepsleep.R
import com.flab.deepsleep.data.entity.photos.SinglePhoto
import com.flab.deepsleep.databinding.ActivityDetailsBinding
import timber.log.Timber
import java.text.NumberFormat
import java.util.Locale

class DetailsActivity : AppCompatActivity() {
    private val detailsBinding: ActivityDetailsBinding by lazy {
        ActivityDetailsBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailsBinding.root)

        val singlePhoto : SinglePhoto? = intent.getParcelableExtra<SinglePhoto>("singlePhoto")

        singlePhoto?.let {
            loadImage(it.urls?.raw)
            bindPhotoDetails(it)
        } ?: run {
            loadImage(null)
            Timber.d("singlePhoto is null")
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
        val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
        val result = numberFormat.format(photo.likes)
        detailsBinding.apply {
            detailDescription.text = photo.description ?: "No description available"
            detailCreateAt.text = photo.createdAt?.take(10) ?: "Unknown date"
            detailLikes.text = result
            detailUsername.text = photo.user?.username
        }
    }
}