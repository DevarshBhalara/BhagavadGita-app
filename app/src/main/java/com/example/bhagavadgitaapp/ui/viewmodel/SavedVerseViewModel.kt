package com.example.bhagavadgitaapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bhagavadgitaapp.services.room.SavedSlok
import com.example.bhagavadgitaapp.ui.repository.SavedSlokRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedVerseViewModel @Inject constructor(
    private val savedSlokRepository: SavedSlokRepository
): ViewModel() {

    private val _savedVerse = MutableStateFlow<List<SavedSlok>>(emptyList())
    val savedVerse = _savedVerse.asStateFlow()

    init {
        getAllSavedVerse()
    }

    private fun getAllSavedVerse() {
        viewModelScope.launch {
            savedSlokRepository.getAllSavedVerse().collectLatest { savedVerse ->
                _savedVerse.emit(savedVerse)
            }
        }
    }

}