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
import com.example.bhagavadgitaapp.data.local.Authors
import com.example.bhagavadgitaapp.databinding.FragmentFragemntViewAllVerseBinding
import com.example.bhagavadgitaapp.listners.ItemClickListener
import com.example.bhagavadgitaapp.ui.adapter.RVVerseTranslationAdapter
import com.example.bhagavadgitaapp.ui.viewmodel.SlokViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentViewAllVerse(private val chapter: Int, private val verse: Int) : Fragment() {

    private lateinit var binding: FragmentFragemntViewAllVerseBinding
    private val viewModel: SlokViewModel by viewModels()
    private val adapter = RVVerseTranslationAdapter()

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
                        Log.e("authors", it.authors.toString())
                        adapter.submitList(it.authors)
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupUI() {
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.shimmerLayout.startShimmer()
        Log.e("data", "$chapter . $verse")

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