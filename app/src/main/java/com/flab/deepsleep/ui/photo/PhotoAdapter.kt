import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flab.deepsleep.R
import com.flab.deepsleep.data.entity.photos.SinglePhoto
import com.flab.deepsleep.databinding.ItemPhotoBinding
import com.flab.deepsleep.ui.photo.OnButtonClick

class PhotoAdapter(private val buttonClick: OnButtonClick) :
    PagingDataAdapter<SinglePhoto, PhotoAdapter.ImageViewHolder>(ARTICLE_DIFF_CALLBACK) {

    inner class ImageViewHolder(
        private val binding: ItemPhotoBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val imageView: ImageView = binding.photoImageView

        fun bind(photo: SinglePhoto) {
            itemView.tag = photo

            /* Like Button */
            binding.btHeart.setOnClickListener {
                buttonClick.onButtonClick(photo)
            }

            val imageUrl = photo.urls?.raw
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
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val photo = getItem(position)
        photo?.let {
            holder.bind(photo)
        }
    }

    /* Paging */
    companion object {
        private val ARTICLE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<SinglePhoto>() {
            override fun areItemsTheSame(oldItem: SinglePhoto, newItem: SinglePhoto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SinglePhoto, newItem: SinglePhoto): Boolean {
                return oldItem == newItem
            }
        }
    }
}