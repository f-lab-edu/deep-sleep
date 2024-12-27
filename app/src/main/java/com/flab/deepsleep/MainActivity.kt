package com.flab.deepsleep

import PhotoAdapter
import android.os.Bundle
import android.widget.ImageView

import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.flab.deepsleep.databinding.ActivityMainBinding
import com.flab.deepsleep.ui.photo.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val photoViewModel: PhotoViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var photoAdapter: PhotoAdapter

    val photoRecyclerView: RecyclerView by lazy {
        binding.photosRecyclerView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* 버튼 누르면 검색 */
        val searchButton: ImageView = findViewById(R.id.search_button)
        searchButton.setOnClickListener{
            val query: String = binding.editText.text.toString()
            photoViewModel.getSearchPhotos(query)
        }

        photoViewModel.searchPhotosList.observe(this, Observer {
            photoAdapter = PhotoAdapter(it)
            setupRecyclerView()
        })

    }// ./onCreate()

    private fun setupRecyclerView() {
        photoRecyclerView.layoutManager = LinearLayoutManager(this)
        photoRecyclerView.adapter = photoAdapter
    }

}