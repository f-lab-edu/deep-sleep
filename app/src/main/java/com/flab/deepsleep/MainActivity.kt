package com.flab.deepsleep

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.flab.deepsleep.ui.photo.PhotoViewModel
import com.flab.deepsleep.utils.setOnTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val photoViewModel: PhotoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imageView: ImageView = findViewById(R.id.testImageView)
        val editText: EditText = findViewById(R.id.editText)
        Timber.plant(Timber.DebugTree())

        // TODO : 버튼 누르면 검색
        val searchButton: ImageView = findViewById(R.id.search_button)
        searchButton.setOnClickListener{
            val query: String = editText.text.toString()
            Timber.d("Timber" + query)
            photoViewModel.getSearchPhotos(query)
        }

        photoViewModel.getARandomPhoto(1)
        photoViewModel.randomphotoUrl.observe(this, Observer {
            val imageUrl = it
            Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        })
    }
}