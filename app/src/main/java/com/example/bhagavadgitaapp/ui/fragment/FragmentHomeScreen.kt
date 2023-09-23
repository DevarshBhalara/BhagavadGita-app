package com.example.bhagavadgitaapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.bhagavadgitaapp.R
import com.example.bhagavadgitaapp.data.local.DisplayRandomSlok
import com.example.bhagavadgitaapp.databinding.FragmentHomeScreenBinding
import com.example.bhagavadgitaapp.databinding.FragmentHomeScreenBindingImpl
import com.example.bhagavadgitaapp.ui.adapter.RVChapterAdapter
import com.example.bhagavadgitaapp.ui.viewmodel.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.floor

@AndroidEntryPoint
class FragmentHomeScreen : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel: HomeScreenViewModel by viewModels()
    private val adapter = RVChapterAdapter()

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

        private fun observers() {
            viewLifecycleOwner.lifecycleScope.launch {
                launch {
                    viewModel.errorMessage.collectLatest {
                        if(it.isNotEmpty()) {
                            Log.e("reponse", it)
                        }
                    }
                }

                launch {
                    viewModel.randomSlok.collectLatest {
                        binding.verse.text = it?.slok
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
        binding.verse.text = getString(R.string.loading)
        val (ch, slok) = calculateRandomSlokNumber()
        viewModel.getRandomSlok(ch, slok)
        binding.rvChapter.adapter = adapter
    }

    private fun calculateRandomSlokNumber(): Pair<Int, Int> {
        val slokCount = listOf(47, 72, 43, 42, 29, 47, 30, 28, 34, 42, 55, 20, 35, 27, 20, 24, 28, 78)
        val chapter = (floor(Math.random() * 17) + 1).toInt()
        val slok = (floor(Math.random() * slokCount[(chapter - 1)]) + 1).toInt()
        return chapter to slok
    }

}