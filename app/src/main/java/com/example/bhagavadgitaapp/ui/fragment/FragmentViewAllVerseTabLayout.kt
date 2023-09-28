package com.example.bhagavadgitaapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.bhagavadgitaapp.data.local.LastRead
import com.example.bhagavadgitaapp.databinding.FragmentViewAllVerseTabLayoutBinding
import com.example.bhagavadgitaapp.helper.PreferenceHelper
import com.example.bhagavadgitaapp.ui.adapter.TabLayoutAdapter
import com.example.bhagavadgitaapp.ui.viewmodel.SlokViewModel
import com.example.bhagavadgitaapp.utils.AppConstants
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentViewAllVerseTabLayout : Fragment() {

    lateinit var binding: FragmentViewAllVerseTabLayoutBinding
    private val navArgs: FragmentViewAllVerseTabLayoutArgs by navArgs()
    private lateinit var preferenceHelper: PreferenceHelper
    private val viewModel: SlokViewModel by  viewModels()
    private lateinit var lastRead: LastRead

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
        bindObservable()
    }

    private fun bindObservable() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.slok.collectLatest { slok ->
                    slok?.let {
                        lastRead = LastRead(
                            navArgs.chapter,
                            it.verse.toString(),
                            it.authors[0].hindiTranslation.toString(),
                            it.authors[1].englishTranslation.toString()
                        )
                        setLastRead(lastRead)
                    }

                }
            }
        }
    }

    private fun setupUI() {
        preferenceHelper = PreferenceHelper(requireContext())
        val verseCount = navArgs.verseCount.toInt()
        val chapter = navArgs.chapter.toInt()
        preferenceHelper.putString(AppConstants.lastChapterVerseCount, verseCount.toString())
        setTabLayout(chapter, verseCount)
    }

    private fun setTabLayout(chapter: Int, verseCount: Int) {
        for (i in 0..verseCount) {
            binding.tabLayout.addTab(binding.tabLayout.newTab())
        }

        val adapter = TabLayoutAdapter(requireActivity().supportFragmentManager, requireActivity(),verseCount, chapter)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = "Verse ${position+1}"
        }.attach()

        if (navArgs.isContinueReading) {
            val tab = binding.tabLayout.getTabAt(preferenceHelper.getString(AppConstants.lastReadVerseNum, "0").toInt() - 1)
            tab?.select()
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position ?: 0
                if (position > 0) {
                    viewModel.getSlok(chapter, position + 1)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Do Nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Do Nothing
            }

        })
    }

    private fun setLastRead(lastRead: LastRead ) {
        preferenceHelper.putBoolean(AppConstants.isLastRead, true)
        preferenceHelper.putString(AppConstants.lastReadChapter, lastRead.chapter)
        preferenceHelper.putString(AppConstants.lastReadVerseNum, lastRead.verse)
        preferenceHelper.putString(AppConstants.lastReadTranslationHindi, lastRead.translationHindi)
        preferenceHelper.putString(AppConstants.lastReadTranslationEnglish, lastRead.translationEnglish)
    }
}