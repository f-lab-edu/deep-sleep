package com.flab.photocollect.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flab.photocollect.ui.activity.MainActivity
import com.flab.photocollect.ui.fragment.BookmarkFragment
import com.flab.photocollect.ui.fragment.HomeFragment
import com.flab.photocollect.utils.Index

class ViewPagerAdapter(mainActivity: MainActivity) : FragmentStateAdapter(mainActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (Index.positionOfIndex(position)) {
            Index.HOME -> HomeFragment()
            Index.BOOKMARK -> BookmarkFragment()
        }
    }
}