package com.example.bhagavadgitaapp.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bhagavadgitaapp.data.local.DisplayRandomSlok
import com.example.bhagavadgitaapp.data.local.HomeScreenLocal
import com.example.bhagavadgitaapp.data.remote.Chapter
import com.example.bhagavadgitaapp.ui.repository.HomeRepository
import com.example.bhagavadgitaapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
  private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _randomSlokDetail = MutableStateFlow<HomeScreenLocal?>(null)
    var randomSlokDetail = _randomSlokDetail.asStateFlow()

    private val _chapters = MutableStateFlow<ArrayList<Chapter>>(arrayListOf())
    var chapters = _chapters.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()


    init {
        getChapter()
    }


    private fun getChapter() {
        viewModelScope.launch {
            homeRepository.getChapters().collectLatest { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        _isLoading.emit(true)
                    }

                    is Resource.Success -> {
                        _isLoading.emit(false)
                        resource.data?.let {
                            _chapters.emit(it)
                        }
                    }

                    is Resource.Error -> {
                        _isLoading.emit(false)
                        resource.message?.let { Log.d("response", it) }
                    }
                }
            }
        }
    }

    fun getRandomSlok(ch: Int, slok: Int) {
        viewModelScope.launch {
            homeRepository.getRandomSlok(ch, slok).collectLatest { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        _isLoading.emit(true)
                    }

                    is Resource.Success -> {
                        _isLoading.emit(false)
                        resource.data?.let { slok ->
                            Log.d("response", slok.slok ?: "No")
                            _randomSlokDetail.emit(
                                HomeScreenLocal(
                                    slok.tej?.ht,
                                    slok.siva?.et,
                                    slok.slok,
                                )
                            )
//                            _randomSlok.emit(slok.siva?.et?.let { DisplayRandomSlok(it, slok.chapter, slok.verse) })
                        }
                    }

                    is Resource.Error -> {
                        _isLoading.emit(false)
                        resource.message?.let {
                            _errorMessage.emit(it)
                        }
                    }
                }
            }
        }
    }
}