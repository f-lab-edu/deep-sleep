package com.flab.photocollect.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.viewpager2.widget.ViewPager2
import com.flab.photocollect.R
import com.flab.photocollect.databinding.ActivityMainBinding
import com.flab.photocollect.ui.adapter.ViewPagerAdapter
import com.flab.photocollect.ui.viewmodel.HomeViewModel
import com.flab.photocollect.utils.Index
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
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
            homeViewModel.searchPhotos(text.toString())
        }

        homeViewModel.errorMessage.observe(this) {
            showErrorDialog(it)
        }
    }

    private fun setViewPager() {
        viewPager.adapter = viewPagerAdapter
        val tabIcons = listOf(R.drawable.ic_home, R.drawable.ic_bookmark)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.icon = ContextCompat.getDrawable(this, tabIcons[position])
            tab.text = when (Index.positionOfIndex(position)) {
                Index.HOME -> getString(R.string.home)
                Index.BOOKMARK -> getString(R.string.bookmark)
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