package com.example.bhagavadgitaapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bhagavadgitaapp.data.remote.Chapter
import com.example.bhagavadgitaapp.databinding.FragmentHomeScreenBinding
import com.example.bhagavadgitaapp.helper.PreferenceHelper
import com.example.bhagavadgitaapp.listners.ItemClickListener
import com.example.bhagavadgitaapp.ui.adapter.RVChapterAdapter
import com.example.bhagavadgitaapp.ui.viewmodel.HomeScreenViewModel
import com.example.bhagavadgitaapp.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.floor

@AndroidEntryPoint
class FragmentHomeScreen : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel: HomeScreenViewModel by viewModels()
    private val adapter = RVChapterAdapter()
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUI()
        observers()
    }

        @SuppressLint("UseCompatLoadingForDrawables")
        private fun observers() {
            viewLifecycleOwner.lifecycleScope.launch {
                launch {
                    viewModel.errorMessage.collectLatest {
                        if(it.isNotEmpty()) {
                            Log.e("error", it)
                        }
                    }
                }

                launch {
                    viewModel.randomSlok.collectLatest {
                        it?.slok?.let {  slok ->
                            binding.verse.text = slok
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.visibility = View.GONE
                            binding.isDataAvailable = true
                        }
                    }
                }

                launch {
                    viewModel.chapters.collectLatest { chapters ->
                        adapter.submitList(chapters)
                    }
                }
            }
        }

    private fun setupUI() {
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.isDataAvailable = false
        preferenceHelper = PreferenceHelper(requireContext())
        preferenceHelper.putString("lan","en")

        getLastRead()

        val (ch, slok) = calculateRandomSlokNumber()
        viewModel.getRandomSlok(ch, slok)
        setupRecyclerView()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
    }

    private fun getLastRead() {
        val isLastReadAvailable = preferenceHelper.getBoolean("isLastRead", false)
        if (isLastReadAvailable) {
            binding.isLastReadAvailable = true
            binding.tvLastReadVerse.text = preferenceHelper.getString(AppConstants.lastReadTranslationEnglish, "")
        } else {
            binding.isLastReadAvailable = false
        }
        Log.e("con", preferenceHelper.getString(AppConstants.lastChapterVerseCount, "1"))
        Log.e("conCh", preferenceHelper.getString(AppConstants.lastReadChapter, "1"))
        binding.btnContinueReading.setOnClickListener {
            val destination = FragmentHomeScreenDirections.actionHomeFragmentToVerse(
                preferenceHelper.getString(AppConstants.lastChapterVerseCount, "1"),
                preferenceHelper.getString(AppConstants.lastReadChapter, "1"),
                true
            )
            findNavController().navigate(destination)
        }
    }

    private fun setupRecyclerView() {
        binding.rvChapter.adapter = adapter
        adapter.itemClickListener = object : ItemClickListener<Chapter> {
            override fun onClick(item: Chapter, position: Int) {
                navigateToChapterDetail(item.chapterNumber)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getLastRead()
    }

    private fun navigateToChapterDetail(item: Int?) {
        item?.let { chapterNumber ->
            val destination = FragmentHomeScreenDirections.actionHomeFragmentToChapterDetail(chapterNumber.toString())
            findNavController().navigate(destination)
        }
    }

    private fun calculateRandomSlokNumber(): Pair<Int, Int> {
        val slokCount = listOf(47, 72, 43, 42, 29, 47, 30, 28, 34, 42, 55, 20, 35, 27, 20, 24, 28, 78)
        val chapter = (floor(Math.random() * 17) + 1).toInt()
        val slok = (floor(Math.random() * slokCount[(chapter - 1)]) + 1).toInt()
        return chapter to slok
    }

}