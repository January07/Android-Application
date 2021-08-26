package com.example.kotlin.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kotlin.fragment.HomeFragment
import com.example.kotlin.fragment.NewsFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0    -> HomeFragment()
            1    -> NewsFragment()
            else -> HomeFragment()
        }
    }
}