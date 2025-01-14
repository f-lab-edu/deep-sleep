package com.flab.deepsleep

import PhotoAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flab.deepsleep.databinding.ActivityMainBinding
import com.flab.deepsleep.ui.details.DetailsActivity
import com.flab.deepsleep.ui.photo.PhotoViewModel
import com.flab.deepsleep.ui.photo.OnButtonClickListener
import com.flab.deepsleep.ui.photo.OnPhotoItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val photoViewModel: PhotoViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val photoRecyclerView: RecyclerView by lazy { binding.photosRecyclerView }
    private val buttonClick = OnButtonClickListener { singlePhoto ->
        photoViewModel.insertPhoto(singlePhoto)
    }
    private val onPhotoItemClickListener =
        OnPhotoItemClickListener { singlePhoto ->
            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
            intent.putExtra("singlePhoto", singlePhoto)  // 객체 전달
            startActivity(intent)
        }
    private val photoAdapter: PhotoAdapter by lazy {
        PhotoAdapter(buttonClick, onPhotoItemClickListener)
    }

    private fun setRecyclerView() {
        photoRecyclerView.layoutManager = GridLayoutManager(this, 2)
        photoRecyclerView.adapter = photoAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setRecyclerView()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoViewModel.items.collectLatest {
                    photoAdapter.submitData(it)
                }
            }
        }

        /* 검색어 입력시 자동 호출 */
        binding.editText.doOnTextChanged { text, start, before, count ->
            photoViewModel.searchPhotos(text.toString())
        }

        /* 에러 관찰 */
        photoViewModel.errorMessage.observe(/* owner = */ this) { it ->
            it?.let {
                showErrorDialog(it)
            }
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}