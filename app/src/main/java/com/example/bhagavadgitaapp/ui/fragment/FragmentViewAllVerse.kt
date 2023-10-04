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
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bhagavadgitaapp.R
import com.example.bhagavadgitaapp.data.local.Authors
import com.example.bhagavadgitaapp.databinding.FragmentFragemntViewAllVerseBinding
import com.example.bhagavadgitaapp.helper.PreferenceHelper
import com.example.bhagavadgitaapp.listners.ItemClickListener
import com.example.bhagavadgitaapp.services.room.SavedSlok
import com.example.bhagavadgitaapp.ui.adapter.RVVerseTranslationAdapter
import com.example.bhagavadgitaapp.ui.viewmodel.SlokViewModel
import com.example.bhagavadgitaapp.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentViewAllVerse(private val chapter: Int, private val verse: Int) : Fragment() {

    private lateinit var binding: FragmentFragemntViewAllVerseBinding
    private val viewModel: SlokViewModel by viewModels()
    private val adapter = RVVerseTranslationAdapter()
    private lateinit var preferenceHelper: PreferenceHelper
    private var isSaved = false
    private var isMenuBarSetup = false

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
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSlok(chapter, verse)
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
        }
        viewModel.getSlok(chapter, verse)
    }
}