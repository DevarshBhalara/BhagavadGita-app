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
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.bhagavadgitaapp.R
import com.example.bhagavadgitaapp.data.local.LastRead
import com.example.bhagavadgitaapp.databinding.FragmentViewAllVerseTabLayoutBinding
import com.example.bhagavadgitaapp.extension.navigate
import com.example.bhagavadgitaapp.helper.PreferenceHelper
import com.example.bhagavadgitaapp.services.room.SavedSlok
import com.example.bhagavadgitaapp.ui.adapter.TabLayoutAdapter
import com.example.bhagavadgitaapp.ui.viewmodel.SlokViewModel
import com.example.bhagavadgitaapp.utils.AppConstants
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class FragmentViewAllVerseTabLayout : Fragment(), MenuProvider {

    lateinit var binding: FragmentViewAllVerseTabLayoutBinding
    private val navArgs: FragmentViewAllVerseTabLayoutArgs by navArgs()
    private lateinit var preferenceHelper: PreferenceHelper
    private val viewModel: SlokViewModel by viewModels()
    private lateinit var lastRead: LastRead
    private var isSaved = false // whether current verse is Saved by user or not
    private var menuLocal: Menu? = null // to later change icon of menu item
    private var currentSavedSlok: SavedSlok? = null

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

            launch {
                viewModel.isSaved.observe(viewLifecycleOwner) {
                    if (isSaved != it) {
                        isSaved = it
                        setupMenuBar()
                    }
                }
            }

            launch {
                viewModel.savedSlokObj.observe(viewLifecycleOwner) { savedSlok ->
                    savedSlok?.let {
                        currentSavedSlok = it
                    }
                }
            }

            launch {
                viewModel.isSavedSuccess.observe(viewLifecycleOwner) {
                    if (it) {
                        isSaved = it
                        menuLocal?.let { menu ->
                            changeMenuIcon(menu)
                        }
                        viewModel.isSlokSaved(preferenceHelper.getString(AppConstants.lastReadChapter, "0"), (binding.tabLayout.selectedTabPosition + 1).toString())
                    }
                }
            }

            launch {
                viewModel.isRemovedSuccess.observe(viewLifecycleOwner) { isRemoved ->
                    if (isRemoved) {
                        isSaved = !isRemoved
                        menuLocal?.let {
                            changeMenuIcon(it)
                        }
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

    /** Menu Bar */
    private fun setupMenuBar() {
        (requireActivity()).addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.STARTED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_slok, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.like -> {
                Log.e("click", "Called $isSaved ${binding.tabLayout.selectedTabPosition}")
                if (!isSaved) {
                    viewModel.saveSlok(
                        SavedSlok(
                            chapterNumber = preferenceHelper.getString(
                                AppConstants.lastReadChapter,
                                "1"
                            ),
                            verseNumber = preferenceHelper.getString(
                                AppConstants.lastReadVerseNum,
                                "1"
                            ),
                            slok = preferenceHelper.getString(
                                AppConstants.lastReadTranslationEnglish,
                                ""
                            ),
                            hindiTranslation = preferenceHelper.getString(
                                AppConstants.lastReadTranslationHindi,
                                ""
                            ),
                            englishTranslation = preferenceHelper.getString(
                                AppConstants.lastReadTranslationEnglish,
                                ""
                            ),
                        )
                    )
                    Snackbar.make(binding.root, "Saved Successfully!", Snackbar.LENGTH_SHORT).show()
                } else {
                    currentSavedSlok?.let {
                        viewModel.removeSlok(it)
                        Snackbar.make(binding.root, "Removed Successfully!", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

            R.id.download -> {
                goToWebViewPage()
            }
        }
        return false
    }

    override fun onPrepareMenu(menu: Menu) {
        menuLocal = menu
        changeMenuIcon(menu)
        super.onPrepareMenu(menu)
    }


    private fun goToWebViewPage() {
        val chapter = preferenceHelper.getString(AppConstants.lastReadChapter, "1")
        val verse = preferenceHelper.getString(AppConstants.lastReadVerseNum, "1")

        try {
            val destination =
                FragmentViewAllVerseTabLayoutDirections.actionVerseToWebViewVerse(AppConstants.BASE_URL + "slok/$chapter/$verse/gita.svg")
            if (isAdded) {
                navigate(destination)
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Please try again late! Server error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun changeMenuIcon(menu: Menu) {
        val item = menu.findItem(R.id.like)
        item?.let {
            if (isSaved) {
                item.icon =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.baseline_bookmark_24)
            } else {
                item.icon = AppCompatResources.getDrawable(requireContext(), R.drawable.baseline_bookmark_border_24)
            }
        }
    }

    private fun setTabLayout(chapter: Int, verseCount: Int) {

        viewModel.isSlokSaved(chapter.toString(), "1")
        setupMenuBar()

        for (i in 0..verseCount) {
            binding.tabLayout.addTab(binding.tabLayout.newTab())
        }

        val adapter = TabLayoutAdapter(
            requireActivity().supportFragmentManager,
            requireActivity(),
            verseCount,
            chapter
        )
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = "Verse ${position + 1}"
        }.attach()

        if (navArgs.isContinueReading) {
            val tab = binding.tabLayout.getTabAt(preferenceHelper.getString(AppConstants.lastReadVerseNum, "0").toInt() - 1)
            tab?.select()
            viewModel.isSlokSaved(chapter.toString(), (tab).toString())
        }

        if(navArgs.isRandomSlok) {
            val tab = binding.tabLayout.getTabAt(preferenceHelper.getString(AppConstants.lastRandomVerseCount, "0").toInt() - 1)
            tab?.select()
            viewModel.isSlokSaved(chapter.toString(), (tab).toString())
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position ?: 0
                if (position > 0) {
                    viewModel.getSlok(chapter, position + 1)
                    viewModel.isSlokSaved(chapter.toString(), (position + 1).toString())
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

    private fun setLastRead(lastRead: LastRead) {
        preferenceHelper.putBoolean(AppConstants.isLastRead, true)
        preferenceHelper.putString(AppConstants.lastReadChapter, lastRead.chapter)
        preferenceHelper.putString(AppConstants.lastReadVerseNum, lastRead.verse)
        preferenceHelper.putString(AppConstants.lastReadTranslationHindi, lastRead.translationHindi)
        preferenceHelper.putString(
            AppConstants.lastReadTranslationEnglish,
            lastRead.translationEnglish
        )
    }
}