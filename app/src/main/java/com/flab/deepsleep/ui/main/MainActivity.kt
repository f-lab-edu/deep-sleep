package com.flab.deepsleep.ui.main

import com.flab.deepsleep.ui.photo.PhotoAdapter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flab.deepsleep.databinding.ActivityMainBinding
import com.flab.deepsleep.ui.details.DetailsActivity
import com.flab.deepsleep.ui.photo.OnButtonClickListener
import com.flab.deepsleep.ui.photo.OnPhotoItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val photoRecyclerView: RecyclerView by lazy { binding.photosRecyclerView }
    private val buttonClick = OnButtonClickListener { uiItem ->
        mainViewModel.insertPhoto(uiItem)
    }

    private val onPhotoItemClickListener =
        OnPhotoItemClickListener { uiItem ->
            DetailsActivity.startActivity(this, uiItem)
        }

    private val photoAdapter: PhotoAdapter by lazy {
        PhotoAdapter(buttonClick, onPhotoItemClickListener)
    }

    private fun setRecyclerView() {
        photoRecyclerView.layoutManager = LinearLayoutManager(this)
        photoRecyclerView.adapter = photoAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setRecyclerView()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.items.collectLatest {
                    photoAdapter.submitData(it)
                }
            }
        }

        /* 검색어 입력시 자동 호출 */
        binding.editText.doOnTextChanged { text, start, before, count ->
            mainViewModel.searchPhotos(text.toString())
        }

        /* 에러 관찰 */
        mainViewModel.errorMessage.observe(this) { it ->
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