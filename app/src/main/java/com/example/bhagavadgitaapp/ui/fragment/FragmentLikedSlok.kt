package com.example.bhagavadgitaapp.ui.fragment

import android.os.Bundle
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
import com.example.bhagavadgitaapp.databinding.FragmentLikedSlokBinding
import com.example.bhagavadgitaapp.ui.adapter.RVSavedVerseAdapter
import com.example.bhagavadgitaapp.ui.viewmodel.SavedVerseViewModel
import com.example.bhagavadgitaapp.ui.viewmodel.SlokViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentLikedSlok : Fragment() {

    private lateinit var binding: FragmentLikedSlokBinding
    private val adapter = RVSavedVerseAdapter()
    private val viewModel: SavedVerseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikedSlokBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        bindObservable()
    }

    private fun bindObservable() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.savedVerse.collectLatest {
                    if (it.isNotEmpty()) {
                        adapter.submitList(it)
                    }
                }
            }
        }
    }

    private fun setupUI() {
        binding.rvLikedVerse.adapter = adapter
    }
}