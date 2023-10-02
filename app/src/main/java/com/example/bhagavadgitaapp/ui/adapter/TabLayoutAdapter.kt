package com.example.bhagavadgitaapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bhagavadgitaapp.ui.fragment.FragmentViewAllVerse

class TabLayoutAdapter(private val fragmentManager: FragmentManager, fa: FragmentActivity, private val totalCount: Int, private val chapter: Int): FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return totalCount
    }

    override fun createFragment(position: Int): Fragment {
        return FragmentViewAllVerse(chapter, position + 1)
    }
}