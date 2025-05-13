package com.multitv.eventbuilder.ui.conference.conferenceGallery.adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.multitv.eventbuilder.ui.conference.conferenceGallery.fragment.FirstTabFragment
import com.multitv.eventbuilder.ui.conference.conferenceGallery.fragment.SecondTabFragment

class TabPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstTabFragment()
            1 -> SecondTabFragment()
            else -> throw IllegalStateException("Invalid tab position")
        }
    }
}