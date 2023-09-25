package com.example.bhagavadgitaapp.ui.repository

import android.util.Log
import com.example.bhagavadgitaapp.data.local.DisplayRandomSlok
import com.example.bhagavadgitaapp.data.remote.Chapter
import com.example.bhagavadgitaapp.data.remote.Slok
import com.example.bhagavadgitaapp.data.services.ApiService
import com.example.bhagavadgitaapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

class HomeRepository(private val apiService: ApiService) : BaseRepository() {

    suspend fun getRandomSlok(ch: Int, slok: Int) = flow<Resource<Slok>> {
        emit(Resource.Loading())
        try {
            apiService.getRandomSlok(ch, slok).let { response ->
                val resource = handleResponse(response)
                emit(resource)
            }
        } catch (e: Exception) {
            Log.d("response", e.localizedMessage)
        }

    }.flowOn(Dispatchers.IO)

    suspend fun getChapters() = flow<Resource<ArrayList<Chapter>>> {
        emit(Resource.Loading())
        try {
            apiService.getChapters().let { response ->
                val resource = handleResponse(response)
                emit(resource)
            }
        } catch (e: Exception) {
            Log.d("response", e.localizedMessage)
        }
    }.flowOn(Dispatchers.IO)
}