import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flab.deepsleep.R
import com.flab.deepsleep.data.entity.photos.SinglePhoto

class PhotoAdapter(private var images: List<String?>) :
    RecyclerView.Adapter<PhotoAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.photoImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = images[position]
        Glide.with(holder.imageView.context)
            .load(imageUrl)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = images.size

    fun updateData(newPhotos: List<String?>) {
        images = newPhotos
        notifyDataSetChanged() // 데이터를 갱신
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<SinglePhoto>() {
            override fun areItemsTheSame(oldItem: SinglePhoto, newItem: SinglePhoto): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SinglePhoto, newItem: SinglePhoto): Boolean =
                oldItem == newItem
        }
    }

}