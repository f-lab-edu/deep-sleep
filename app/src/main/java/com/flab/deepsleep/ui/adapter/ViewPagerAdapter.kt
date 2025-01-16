package com.flab.deepsleep.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flab.deepsleep.MainActivity
import com.flab.deepsleep.ui.fragment.BookmarkFragment
import com.flab.deepsleep.ui.fragment.HomeFragment

class ViewPagerAdapter(mainActivity: MainActivity) : FragmentStateAdapter(mainActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> BookmarkFragment()
            else -> HomeFragment()
        }
    }

}