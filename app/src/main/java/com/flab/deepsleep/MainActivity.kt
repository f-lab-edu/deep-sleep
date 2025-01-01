package com.flab.deepsleep

import PhotoAdapter
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flab.deepsleep.databinding.ActivityMainBinding
import com.flab.deepsleep.ui.photo.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val photoViewModel: PhotoViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val photoRecyclerView: RecyclerView by lazy {
        binding.photosRecyclerView
    }
    private val photoAdapter: PhotoAdapter by lazy {
        PhotoAdapter(emptyList())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* 버튼 누르면 검색 */
        val searchButton: ImageView = binding.searchButton
        searchButton.setOnClickListener{
            val query: String = binding.editText.text.toString()
            photoViewModel.getSearchPhotos(query)
        }

        photoViewModel.searchPhotosList.observe(this, Observer {
            photoAdapter.updateData(it)
            setupRecyclerView()
        })

        /* 에러 관찰 */
        photoViewModel.errorMessage.observe(this, Observer {
                it -> it?.let {
            showErrorDialog(it)
        }
        })
    }// ./onCreate()

    private fun setupRecyclerView() {
        photoRecyclerView.layoutManager = LinearLayoutManager(this)
        photoRecyclerView.adapter = photoAdapter
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