package com.example.bhagavadgitaapp.ui.fragment

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bhagavadgitaapp.R
import com.example.bhagavadgitaapp.data.local.ChapterDetailLocal
import com.example.bhagavadgitaapp.databinding.FragmentChapterDetailBinding
import com.example.bhagavadgitaapp.helper.PreferenceHelper
import com.example.bhagavadgitaapp.ui.viewmodel.ChapterDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentChapterDetail : Fragment(), MenuProvider {

    private lateinit var binding: FragmentChapterDetailBinding
    private val viewModel: ChapterDetailViewModel by viewModels()
    private val navArgs: FragmentChapterDetailArgs by navArgs()
    private var chapterNumber: String = ""
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var chapterLocal: ChapterDetailLocal
    private var verseCount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChapterDetailBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUI()
        setupObservable()
    }

    private fun setupObservable() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.chapter.collectLatest { chapter ->
                    chapter?.let {
                        verseCount = chapter.versesCount ?: 0
                        val selectedLanguage = preferenceHelper.getString("lan", "en")
                        if (selectedLanguage == "hi") {
                            it.userSelectedLanguageMeaning = it.meaningHindi
                            it.userSelectedLanguageSummary = it.summaryHindi
                        } else {
                            it.userSelectedLanguageMeaning = it.meaningEnglish
                            it.userSelectedLanguageSummary = it.summaryEnglish
                        }
                        chapterLocal = it
                        binding.chapter = chapterLocal
                        binding.loadingIndicator.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("lanCh", preferenceHelper.getString("lan", "NA"))
    }

    private fun setupUI() {
        preferenceHelper = PreferenceHelper(requireContext())
        chapterNumber = navArgs.chapterNumber
        binding.btnViewAllVerse.setOnClickListener {
            val destination = FragmentChapterDetailDirections.actionChapterDetailToVerse(verseCount.toString(), chapterNumber, false)
            findNavController().navigate(destination)
        }
        getChapterDetails()
        setupMenuBar()
        handleNextPrevious()

    }

    private fun getChapterDetails() {
        viewModel.getChapter(chapterNumber)
    }

    private fun handleNextPrevious() {
        if (chapterNumber.isNotEmpty() && chapterNumber.toInt() in 1..18) {
            binding.next.setOnClickListener {
                binding.loadingIndicator.visibility = View.VISIBLE
                if (chapterNumber.toInt() == 18) {
                    chapterNumber = "1"
                    getChapterDetails()
                } else {
                    chapterNumber = ((chapterNumber.toInt()) + 1).toString()
                    getChapterDetails()
                }
            }

            binding.previous.setOnClickListener {
                binding.loadingIndicator.visibility = View.VISIBLE
                if (chapterNumber.toInt() == 1) {
                    chapterNumber = "18"
                    getChapterDetails()
                } else {
                    chapterNumber = ((chapterNumber.toInt()) - 1).toString()
                    getChapterDetails()
                }
            }
        }
    }

    private fun setupMenuBar() {
        (requireActivity() as MenuHost).addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun changeLanguage() {
        val selectedLanguage = preferenceHelper.getString("lan", "en")
        if (selectedLanguage == "hi") {
            chapterLocal.userSelectedName = chapterLocal.name
            chapterLocal.userSelectedLanguageMeaning = chapterLocal.meaningHindi
            chapterLocal.userSelectedLanguageSummary = chapterLocal.summaryHindi
        } else {
            chapterLocal.userSelectedName = chapterLocal.translation
            chapterLocal.userSelectedLanguageMeaning = chapterLocal.meaningEnglish
            chapterLocal.userSelectedLanguageSummary = chapterLocal.summaryEnglish
        }
        binding.chapter = chapterLocal
        binding.executePendingBindings()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.chapter_detial_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId) {
            R.id.hindi -> {
                preferenceHelper.putString("lan", "hi")
                changeLanguage()
            }
            R.id.english -> {
                preferenceHelper.putString("lan", "en")
                changeLanguage()
            }
        }
        return false
    }
}