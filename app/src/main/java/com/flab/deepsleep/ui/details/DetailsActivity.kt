package com.flab.deepsleep.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.flab.deepsleep.R
import com.flab.deepsleep.data.entity.photos.SinglePhoto
import com.flab.deepsleep.databinding.ActivityDetailsBinding
import com.flab.deepsleep.ui.photo.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val detailsBinding: ActivityDetailsBinding by lazy {
        ActivityDetailsBinding.inflate(layoutInflater)
    }
    private val photoViewModel: PhotoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailsBinding.root)

        val singlePhoto: SinglePhoto? =
            @Suppress("DEPRECATION") intent.getParcelableExtra("singlePhoto")

        singlePhoto?.let {
            loadImage(it.urls?.raw)
            bindPhotoDetails(it)
            singlePhoto.id?.let { photoViewModel.loadPhotoLikeStatus(it) }
        } ?: run {
            loadImage(null)
            Timber.d("singlePhoto is null")
        }

        photoViewModel.isLiked.observe(this) { isLiked ->
            detailsBinding.detailBtHeart.isSelected = isLiked
        }
    }

    private fun loadImage(imageUrl: String?) {
        val placeholderImage = R.drawable.ic_launcher_foreground
        Glide.with(detailsBinding.detailsImageView.context)
            .load(imageUrl ?: placeholderImage)
            .placeholder(placeholderImage)
            .into(detailsBinding.detailsImageView)
    }

    private fun bindPhotoDetails(singlePhoto: SinglePhoto) {
        val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
        val result = numberFormat.format(singlePhoto.likes)
        detailsBinding.apply {
            detailDescription.text = singlePhoto.description ?: "No description available"
            detailCreateAt.text = singlePhoto.createdAt?.take(10) ?: "Unknown date"
            detailLikes.text = result
            detailUsername.text = singlePhoto.user?.username
        }

        detailsBinding.detailBtHeart.setOnClickListener {
            photoViewModel.insertPhoto(singlePhoto)
        }
    }

    companion object {
        fun startDetailsActivity(context: Context, singlePhoto: SinglePhoto) {
            val intent = Intent(context, DetailsActivity::class.java).apply {
                putExtra("singlePhoto", singlePhoto)
            }
            context.startActivity(intent)
        }
    }
}