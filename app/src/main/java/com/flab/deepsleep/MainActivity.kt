package com.flab.deepsleep

import PhotoAdapter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flab.deepsleep.databinding.ActivityMainBinding
import com.flab.deepsleep.ui.photo.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val photoViewModel: PhotoViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val photoRecyclerView: RecyclerView by lazy { binding.photosRecyclerView }
    private val photoAdapter: PhotoAdapter by lazy { PhotoAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setRecyclerView()

        lifecycleScope.launch {
            photoViewModel.items.collectLatest { pagingData ->
                photoAdapter.submitData(pagingData)
            }
        }

        /* 검색어 입력시 자동 호출 */
        binding.editText.doOnTextChanged { text, start, before, count ->
            photoViewModel.setQuery(text.toString())
        }

        /* 에러 관찰 */
        photoViewModel.errorMessage.observe(this, Observer { it ->
            it?.let {
                showErrorDialog(it)
            }
        })
    }

    private fun setRecyclerView() {
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