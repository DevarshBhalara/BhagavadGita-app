package com.example.bhagavadgitaapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bhagavadgitaapp.data.local.ChapterDetailLocal
import com.example.bhagavadgitaapp.data.remote.ChapterDetail
import com.example.bhagavadgitaapp.ui.repository.ChapterDetailRepository
import com.example.bhagavadgitaapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterDetailViewModel @Inject constructor(
    private val chapterDetailRepository: ChapterDetailRepository
): ViewModel() {

    private val _chapter = MutableStateFlow<ChapterDetailLocal?>(null)
    var chapter = _chapter.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    fun getChapter(chapterNumber: String) {
        viewModelScope.launch {
            chapterDetailRepository.getChapterDetails(chapterNumber).collectLatest { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        _isLoading.emit(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let {
                            _chapter.emit(ChapterDetailLocal(
                                it.chapterNumber,
                                it.versesCount,
                                it.name,
                                it.translation,
                                it.transliteration,
                                it.meaning?.hi,
                                it.meaning?.en,
                                it.summary?.hi,
                                it.summary?.en
                            ))
                        }
                    }

                    is Resource.Error -> {
                        _errorMessage.emit(resource.message ?: "Please try Again!")
                    }
                }
            }
        }
    }

}