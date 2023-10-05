package com.example.bhagavadgitaapp.ui.repository

import android.util.Log
import com.example.bhagavadgitaapp.data.remote.Slok
import com.example.bhagavadgitaapp.data.services.ApiService
import com.example.bhagavadgitaapp.services.room.SavedSlok
import com.example.bhagavadgitaapp.services.room.SavedSlokDao
import com.example.bhagavadgitaapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

class SlokRepository(private val apiService: ApiService, private val slokDao: SavedSlokDao): BaseRepository() {

    suspend fun getSlok(ch: Int, slok: Int) = flow<Resource<Slok>> {
        emit(Resource.Loading())
        try {
            apiService.getSlok(ch, slok).let { response ->
                val resource = handleResponse(response)
                emit(resource)
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "Some Error Occurred"))
            e.localizedMessage?.let { Log.d("response", it) }
        }

    }.flowOn(Dispatchers.IO)

    suspend fun getVerseImage(ch: String, slok: String) = flow<Resource<String>> {
        emit(Resource.Loading())
        try {
            apiService.getVerseImage(ch, slok).let { response ->
                val resource = handleResponse(response)
                emit(resource)
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "Error"))
            e.localizedMessage?.let { Log.d("response", it) }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun isSavedSlok(chapter: String, verse: String) = flow<SavedSlok?> {
        val sloks = slokDao.isSlokSaved(chapter, verse)
        emit(sloks.firstOrNull())
    }.flowOn(Dispatchers.IO)

    suspend fun saveSlok(savedSlok: SavedSlok) = flow<Boolean> {
        val sloks = slokDao.insertAll(savedSlok)
        emit(sloks.first() > 0)
    }.flowOn(Dispatchers.IO)

    suspend fun removeSlok(savedSlok: SavedSlok) = flow<Boolean> {
        val verse = slokDao.removeSaved(savedSlok)
        emit(verse > 0)
    }.flowOn(Dispatchers.IO)
}