package com.flab.photocollect.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flab.photocollect.ui.activity.MainActivity
import com.flab.photocollect.ui.fragment.BookmarkFragment
import com.flab.photocollect.ui.fragment.HomeFragment
import com.flab.photocollect.utils.Page

class ViewPagerAdapter(mainActivity: MainActivity) : FragmentStateAdapter(mainActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (Page.positionOfPage(position)) {
            Page.HOME -> HomeFragment()
            Page.BOOKMARK -> BookmarkFragment()
        }
    }
}