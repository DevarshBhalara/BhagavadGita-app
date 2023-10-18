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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.example.bhagavadgitaapp.databinding.FragmentLikedSlokBinding
import com.example.bhagavadgitaapp.extension.navigate
import com.example.bhagavadgitaapp.listners.ItemClickListener
import com.example.bhagavadgitaapp.services.room.SavedSlok
import com.example.bhagavadgitaapp.ui.activity.MainActivity
import com.example.bhagavadgitaapp.ui.adapter.RVSavedVerseAdapter
import com.example.bhagavadgitaapp.ui.viewmodel.SavedVerseViewModel
import com.example.bhagavadgitaapp.ui.viewmodel.SlokViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentLikedSlok : Fragment(), MenuProvider {

    private lateinit var binding: FragmentLikedSlokBinding
    private val adapter = RVSavedVerseAdapter()
    private val viewModel: SavedVerseViewModel by viewModels()
    private val slokViewModel: SlokViewModel by viewModels()

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
                        binding.tvNoSaved.visibility = View.GONE
                        adapter.submitList(it)
                    } else {
                        binding.tvNoSaved.visibility = View.VISIBLE
                        adapter.submitList(it)
                    }
                }
            }
            launch {
                slokViewModel.isRemovedSuccess.observe(viewLifecycleOwner) {
                    if(it) {
                        viewModel.getAllSavedVerse()
                    }
                }
            }

            launch {
                slokViewModel.isSavedSuccess.observe(viewLifecycleOwner) {
                    if (it) {
                        viewModel.getAllSavedVerse()
                    }
                }
            }
        }
    }

    private fun setupUI() {
        setupMenuBar()
        (requireActivity() as MainActivity).title = "Your Saved Verse"
        binding.rvLikedVerse.adapter = adapter
        adapter.itemClickListener = object : ItemClickListener<SavedSlok> {
            override fun onClick(item: SavedSlok, position: Int) {
                val destination = FragmentLikedSlokDirections.actionLikedToVerseDetail(item.verseNumber, item.chapterNumber)
                navigate(destination)
            }

            override fun onLikeClick(item: SavedSlok, position: Int) {
                addOrRemoveLike(item, position)
            }

        }
    }

    private fun addOrRemoveLike(slok: SavedSlok, position: Int) {
        slokViewModel.removeSlok(slok)
        Snackbar.make(binding.root, "Remove Successfully!", Snackbar.LENGTH_SHORT).setAction("Undo") {
            slokViewModel.saveSlok(slok)
        }.show()
    }

    private fun setupMenuBar() {
        (requireActivity() as MenuHost).addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
    }
}