package com.example.bhagavadgitaapp.ui.repository

import com.example.bhagavadgitaapp.services.room.SavedSlokDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SavedSlokRepository(private val slokDao: SavedSlokDao) {

    suspend fun getAllSavedVerse() = flow {
        val savedVerse = slokDao.getAll()
        emit(savedVerse)
    }.flowOn(Dispatchers.IO)

}