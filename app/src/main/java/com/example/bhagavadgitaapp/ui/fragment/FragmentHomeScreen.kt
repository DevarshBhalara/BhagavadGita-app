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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bhagavadgitaapp.R
import com.example.bhagavadgitaapp.data.local.HomeScreenLocal
import com.example.bhagavadgitaapp.data.remote.Chapter
import com.example.bhagavadgitaapp.databinding.FragmentHomeScreenBinding
import com.example.bhagavadgitaapp.helper.CustomClassData
import com.example.bhagavadgitaapp.helper.PreferenceHelper
import com.example.bhagavadgitaapp.listners.ItemClickListener
import com.example.bhagavadgitaapp.ui.adapter.RVChapterAdapter
import com.example.bhagavadgitaapp.ui.viewmodel.HomeScreenViewModel
import com.example.bhagavadgitaapp.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.floor

@AndroidEntryPoint
class FragmentHomeScreen : Fragment(), MenuProvider {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel: HomeScreenViewModel by viewModels()
    private val adapter = RVChapterAdapter()
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var slokDetailLocal: HomeScreenLocal
    private var totalVerseRandom = ""
    private lateinit var customClassData: CustomClassData

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
                    if (it.isNotEmpty()) {
                        Log.e("error", it)
                    }
                }
            }

            launch {
                viewModel.randomSlokDetail.collectLatest {
                    it?.let {
                        val userLanguage = preferenceHelper.getString("lan", "en")
                        it.lastReadSlokHindi =
                            preferenceHelper.getString(AppConstants.lastReadTranslationHindi, "")
                        it.lastReadSlokEnglish =
                            preferenceHelper.getString(AppConstants.lastReadTranslationEnglish, "")
                        if (userLanguage == "en") {
                            it.userSelectedLanguageSlok = it.slokEnglish
                            it.lastReadUserLanguage = it.lastReadSlokEnglish
                        } else {
                            it.userSelectedLanguageSlok = it.slokHindi
                            it.lastReadUserLanguage = it.lastReadSlokHindi
                        }
                        slokDetailLocal = it
                        binding.slokData = slokDetailLocal
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.isDataAvailable = true

                        preferenceHelper.putString(AppConstants.lastRandomVerseCount, it.verseNumber.toString())
                        it.chapter?.let { it1 -> viewModel.getChapter(it1) }
                        customClassData.saveCustomClass(slokDetailLocal)
                    }
                }
            }

            launch {
                viewModel.totalVerse.collectLatest { totalVerse ->
                    if (totalVerse.isNotEmpty()) {
                        totalVerseRandom = totalVerse
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


    override fun onPause() {
        super.onPause()
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
    }


    override fun onResume() {
        super.onResume()
        preferenceHelper.putString(AppConstants.currentDate, getCurrentDate())
    }


    private fun setupUI() {
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.isDataAvailable = false
        preferenceHelper = PreferenceHelper(requireContext())
        customClassData = CustomClassData(requireContext())
        setupMenuBar()
        if (preferenceHelper.getString(AppConstants.currentDate, "") == getCurrentDate()) {
            customClassData.getCustomClass()?.let {
                slokDetailLocal = it
                changeLanguage()
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.GONE
                binding.isDataAvailable = true

                preferenceHelper.putString(AppConstants.lastRandomVerseCount, it.verseNumber.toString())
                it.chapter?.let { it1 -> viewModel.getChapter(it1) }
            }
        } else {
            val (ch, slok) = calculateRandomSlokNumber()
            viewModel.getRandomSlok(ch, slok)
        }
        getLastRead()
        setupRecyclerView()

        binding.btnReadThis.setOnClickListener {
            val destination = FragmentHomeScreenDirections.actionHomeFragmentToVerse(
                totalVerseRandom,
                slokDetailLocal.chapter.toString(),
                false,
                true
            )
            findNavController().navigate(destination)
        }
    }

    private fun setupMenuBar() {
        (requireActivity() as MenuHost).addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    private fun getLastRead() {
        binding.isLastReadAvailable = preferenceHelper.getBoolean("isLastRead", false)
        if (binding.isLastReadAvailable) {
            binding.isLastReadAvailable = true
        }

        binding.btnContinueReading.setOnClickListener {
            val destination = FragmentHomeScreenDirections.actionHomeFragmentToVerse(
                preferenceHelper.getString(AppConstants.lastChapterVerseCount, "1"),
                preferenceHelper.getString(AppConstants.lastReadChapter, "1"),
                true,
                false
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

            override fun onLikeClick(item: Chapter, position: Int) {
                // Nothing
            }
        }
    }

    private fun navigateToChapterDetail(item: Int?) {
        item?.let { chapterNumber ->
            val destination =
                FragmentHomeScreenDirections.actionHomeFragmentToChapterDetail(chapterNumber.toString())
            findNavController().navigate(destination)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.chapter_detial_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
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


    private fun changeLanguage() {
        if (preferenceHelper.getString("lan", "en") == "en") {
            slokDetailLocal.userSelectedLanguageSlok = slokDetailLocal.slokEnglish
            slokDetailLocal.lastReadUserLanguage = preferenceHelper.getString(AppConstants.lastReadTranslationEnglish, "NA")
        } else {
            slokDetailLocal.userSelectedLanguageSlok = slokDetailLocal.slokHindi
            slokDetailLocal.lastReadUserLanguage = preferenceHelper.getString(AppConstants.lastReadTranslationHindi, "NA")
        }
        customClassData.saveCustomClass(slokDetailLocal)
        binding.slokData = slokDetailLocal
        binding.executePendingBindings()
    }

    @SuppressLint("NewApi")
    fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Define the date format you want
        return currentDate.format(formatter)
    }

    private fun calculateRandomSlokNumber(): Pair<Int, Int> {
        val slokCount =
            listOf(47, 72, 43, 42, 29, 47, 30, 28, 34, 42, 55, 20, 35, 27, 20, 24, 28, 78)
        val chapter = (floor(Math.random() * 17) + 1).toInt()
        val slok = (floor(Math.random() * slokCount[(chapter - 1)]) + 1).toInt()
        return chapter to slok
    }


}