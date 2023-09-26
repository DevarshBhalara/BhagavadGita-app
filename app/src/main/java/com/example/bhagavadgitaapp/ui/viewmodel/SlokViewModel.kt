package com.example.bhagavadgitaapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bhagavadgitaapp.data.local.Authors
import com.example.bhagavadgitaapp.data.local.SlokLocal
import com.example.bhagavadgitaapp.data.local.TranslationOrCommentary
import com.example.bhagavadgitaapp.data.remote.Slok
import com.example.bhagavadgitaapp.ui.repository.SlokRepository
import com.example.bhagavadgitaapp.utils.AppConstants
import com.example.bhagavadgitaapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SlokViewModel @Inject constructor(
    private val slokRepository: SlokRepository
): ViewModel() {

    private val _slok = MutableStateFlow<SlokLocal?>(null)
    var slok = _slok.asStateFlow()

    fun getSlok(ch: Int, sl: Int) {
        viewModelScope.launch {
            slokRepository.getSlok(ch, sl).collectLatest { resourse ->
                when(resourse) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        resourse.data?.let { slok ->
                            val authors = getAuthors(slok)
                            _slok.emit(SlokLocal(
                                id = slok.Id,
                                chapter = slok.chapter,
                                verse = slok.verse,
                                slok = slok.slok,
                                transliteration = slok.transliteration,
                                authors = authors,
                            ))
                        }
                    }
                    is Resource.Error -> {

                    }
                }
            }
        }
    }

    private fun getAuthors(slok: Slok): MutableList<Authors> {
        val authors = mutableListOf<Authors>()
        authors.add(
            Authors(
                slok.tej?.author ?: "N/A",
                hindiTranslation = slok.tej?.ht,
                totalCommentaryTranslation = 1,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.HT, slok.tej?.ht)
                )
            )
        )
        authors.add(
            Authors(
                slok.siva?.author ?: "N/A",
                englishTranslation = slok.siva?.et,
                englishCommentary = slok.siva?.ec,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.ET, slok.siva?.et),
                    TranslationOrCommentary(AppConstants.EC, slok.siva?.ec)
                )
            )
        )
        authors.add(
            Authors(
                slok.purohit?.author ?: "N/A",
                englishTranslation = slok.purohit?.et,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.ET, slok.purohit?.et),
                )
            )
        )
        authors.add(
            Authors(
                slok.chinmay?.author ?: "N/A",
                hindiCommentary = slok.chinmay?.hc,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.HC, slok.chinmay?.hc),
                )
            )
        )

        authors.add(
            Authors(
                slok.san?.author ?: "N/A",
                englishTranslation = slok.san?.et,
                isExpanded = false,
                translation = mutableListOf(
                        TranslationOrCommentary(AppConstants.ET, slok.san?.et),
                )
            )
        )

        authors.add(
            Authors(
                slok.adi?.author ?: "N/A",
                englishTranslation = slok.adi?.et,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.ET, slok.adi?.et),
                )
            )
        )

        authors.add(
            Authors(
                slok.gambir?.author ?: "N/A",
                englishTranslation = slok.gambir?.et,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.ET, slok.gambir?.et),
                )
            )
        )

        authors.add(
            Authors(
                slok.madhav?.author ?: "N/A",
                sanskritCommentary = slok.madhav?.sc,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.SC, slok.madhav?.sc),
                )
            )
        )

        authors.add(
            Authors(
                slok.anand?.author ?: "N/A",
                sanskritCommentary = slok.anand?.sc,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.SC, slok.anand?.sc),
                )
            )
        )

        authors.add(
            Authors(
                slok.rams?.author ?: "N/A",
                hindiTranslation = slok.rams?.ht,
                hindiCommentary = slok.rams?.hc,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.HT, slok.rams?.ht),
                    TranslationOrCommentary(AppConstants.HC, slok.rams?.hc),
                )
            )
        )
        authors.add(
            Authors(
                slok.raman?.author ?: "N/A",
                sanskritCommentary = slok.raman?.sc,
                englishTranslation = slok.raman?.et,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.SC, slok.raman?.sc),
                    TranslationOrCommentary(AppConstants.ET, slok.raman?.et),
                )
            )
        )
        authors.add(
            Authors(
                slok.abhinav?.author ?: "N/A",
                sanskritCommentary = slok.abhinav?.sc,
                englishTranslation = slok.abhinav?.et,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.SC, slok.abhinav?.sc),
                    TranslationOrCommentary(AppConstants.ET, slok.abhinav?.et),
                )
            )
        )
        authors.add(
            Authors(
                slok.sankar?.author ?: "N/A",
                sanskritCommentary = slok.sankar?.sc,
                englishTranslation = slok.sankar?.et,
                hindiTranslation = slok.sankar?.ht,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.SC, slok.sankar?.sc),
                    TranslationOrCommentary(AppConstants.ET, slok.sankar?.et),
                    TranslationOrCommentary(AppConstants.HT, slok.sankar?.ht),
                )
            )
        )
        authors.add(
            Authors(
                slok.jaya?.author ?: "N/A",
                sanskritCommentary = slok.jaya?.sc,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.SC, slok.jaya?.sc),
                )
            )
        )
        authors.add(
            Authors(
                slok.vallabh?.author ?: "N/A",
                sanskritCommentary = slok.vallabh?.sc,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.SC, slok.vallabh?.sc),
                )
            )
        )
        authors.add(
            Authors(
                slok.ms?.author ?: "N/A",
                sanskritCommentary = slok.ms?.sc,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.SC, slok.ms?.sc),
                )
            )
        )
        authors.add(
            Authors(
                slok.srid?.author ?: "N/A",
                sanskritCommentary = slok.srid?.sc,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.SC, slok.srid?.sc),
                )
            )
        )
        authors.add(
            Authors(
                slok.dhan?.author ?: "N/A",
                sanskritCommentary = slok.dhan?.sc,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.SC, slok.dhan?.sc),
                )
            )
        )
        authors.add(
            Authors(
                slok.venkat?.author ?: "N/A",
                sanskritCommentary = slok.venkat?.sc,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.SC, slok.venkat?.sc),
                )
            )
        )
        authors.add(
            Authors(
                slok.puru?.author ?: "N/A",
                sanskritCommentary = slok.puru?.sc,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.SC, slok.puru?.sc),
                )
            )
        )
        authors.add(
            Authors(
                slok.neel?.author ?: "N/A",
                sanskritCommentary = slok.neel?.sc,
                isExpanded = false,
                translation = mutableListOf(
                    TranslationOrCommentary(AppConstants.SC, slok.neel?.sc),
                )
            )
        )
        return authors
    }

}