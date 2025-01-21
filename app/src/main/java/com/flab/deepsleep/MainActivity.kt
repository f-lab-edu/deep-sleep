package com.flab.deepsleep

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.viewpager2.widget.ViewPager2
import com.flab.deepsleep.databinding.ActivityMainBinding
import com.flab.deepsleep.ui.adapter.ViewPagerAdapter
import com.flab.deepsleep.ui.photo.PhotoViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val photoViewModel: PhotoViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val tabLayout: TabLayout by lazy { binding.tabLayout }
    private val viewPager: ViewPager2 by lazy { binding.viewPager }
    private val viewPagerAdapter: ViewPagerAdapter by lazy { ViewPagerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setViewPager()

        /* 검색어 입력시 자동 호출 */
        binding.editText.doOnTextChanged { text, start, before, count ->
            photoViewModel.searchPhotos(text.toString())
        }

        /* 에러 관찰 */
        photoViewModel.errorMessage.observe(/* owner = */ this) { it ->
            it?.let { showErrorDialog(it) }
        }
    }

    private fun setViewPager() {
        viewPager.adapter = viewPagerAdapter
        val tabIcons = listOf(R.drawable.ic_home, R.drawable.ic_bookmark)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.icon = ContextCompat.getDrawable(this, tabIcons[position])
            tab.text = when (position) {
                0 -> getString(R.string.home)
                1 -> getString(R.string.bookmark)
                else -> "탭 ${position + 1}"
            }
        }.attach()
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