package com.example.bhagavadgitaapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.bhagavadgitaapp.data.local.Authors
import com.example.bhagavadgitaapp.data.local.LastRead
import com.example.bhagavadgitaapp.databinding.FragmentFragemntViewAllVerseBinding
import com.example.bhagavadgitaapp.helper.PreferenceHelper
import com.example.bhagavadgitaapp.listners.ItemClickListener
import com.example.bhagavadgitaapp.ui.adapter.RVVerseTranslationAdapter
import com.example.bhagavadgitaapp.ui.viewmodel.SlokViewModel
import com.example.bhagavadgitaapp.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentViewAllVerse() : Fragment(), MenuProvider {

    constructor(chapter: Int, verse: Int) : this() {
        this.chapter = chapter
        this.verse = verse
    }

    private var chapter: Int = -1
    private var verse: Int = -1
    private val navArgs: FragmentViewAllVerseArgs by navArgs()
    private lateinit var binding: FragmentFragemntViewAllVerseBinding
    private val viewModel: SlokViewModel by viewModels()
    private val adapter = RVVerseTranslationAdapter()
    private lateinit var preferenceHelper: PreferenceHelper
    private var lastRead: LastRead? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFragemntViewAllVerseBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUI()
        bindObservables()
    }

    private fun bindObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.slok.collectLatest { slok ->
                    slok?.let {
                        binding.verse = it
                        adapter.submitList(it.authors)
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE

                        lastRead = LastRead(
                            chapter.toString(),
                            it.verse.toString(),
                            it.authors[0].hindiTranslation.toString(),
                            it.authors[1].englishTranslation.toString()
                        )
                        setLastRead(lastRead!!)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (chapter == -1 && verse == -1) {
            viewModel.getSlok(navArgs.chapter.toInt(), navArgs.verse.toInt())
        } else {
            viewModel.getSlok(chapter, verse)
        }
        lastRead?.let {
            setLastRead(it)
        }
    }

    private fun setLastRead(lastRead: LastRead) {
        preferenceHelper.putBoolean(AppConstants.isLastRead, true)
        preferenceHelper.putString(AppConstants.lastReadChapter, lastRead.chapter)
        preferenceHelper.putString(AppConstants.lastReadVerseNum, lastRead.verse)
        preferenceHelper.putString(AppConstants.lastReadTranslationHindi, lastRead.translationHindi)
        preferenceHelper.putString(
            AppConstants.lastReadTranslationEnglish,
            lastRead.translationEnglish
        )
        Log.e("last", lastRead.toString())
    }

    private fun setupUI() {
        preferenceHelper = PreferenceHelper(requireContext())
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.shimmerLayout.startShimmer()

        binding.rvVerseTranslation.adapter = adapter
        adapter.itemClickListener = object : ItemClickListener<Authors> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onClick(item: Authors, position: Int) {
                item.isExpanded = !item.isExpanded
                adapter.notifyItemChanged(position)
            }

            override fun onLikeClick(item: Authors, position: Int) {
                // No thing
            }
        }
        if (chapter == -1 && verse == -1) {
            viewModel.getSlok(navArgs.chapter.toInt(), navArgs.verse.toInt())
        } else {
            viewModel.getSlok(chapter, verse)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}