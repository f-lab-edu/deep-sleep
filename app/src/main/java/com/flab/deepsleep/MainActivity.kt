package com.flab.deepsleep

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.flab.deepsleep.databinding.ActivityMainBinding
import com.flab.deepsleep.ui.home.HomeFragment
import com.flab.deepsleep.ui.photo.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val photoViewModel: PhotoViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setHomeFragment();

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

    private fun setHomeFragment(){
        supportFragmentManager.beginTransaction()
            .replace(binding.mainFragment.id, HomeFragment())
            .commit()
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