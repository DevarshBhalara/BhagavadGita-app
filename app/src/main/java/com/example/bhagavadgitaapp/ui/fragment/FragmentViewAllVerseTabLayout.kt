package com.example.bhagavadgitaapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.bhagavadgitaapp.databinding.FragmentViewAllVerseTabLayoutBinding
import com.example.bhagavadgitaapp.ui.adapter.TabLayoutAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentViewAllVerseTabLayout : Fragment() {

    lateinit var binding: FragmentViewAllVerseTabLayoutBinding
    private val navArgs: FragmentViewAllVerseTabLayoutArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewAllVerseTabLayoutBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUI()
    }

    private fun setupUI() {
        val verseCount = navArgs.verseCount.toInt()
        val chapter = navArgs.chapter.toInt()

        for (i in 0..verseCount) {
            binding.tabLayout.addTab(binding.tabLayout.newTab())
        }

        val adapter = TabLayoutAdapter(requireActivity(),verseCount, chapter)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = "Verse ${position+1}"
        }.attach()
    }
}