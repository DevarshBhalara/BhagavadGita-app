package com.example.bhagavadgitaapp.ui.repository

import android.util.Log
import com.example.bhagavadgitaapp.data.remote.Slok
import com.example.bhagavadgitaapp.data.services.ApiService
import com.example.bhagavadgitaapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

class SlokRepository(private val apiService: ApiService): BaseRepository() {

    suspend fun getSlok(ch: Int, slok: Int) = flow<Resource<Slok>> {
        emit(Resource.Loading())
        try {
            apiService.getSlok(ch, slok).let { response ->
                val resource = handleResponse(response)
                emit(resource)
            }
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d("response", it) }
        }

    }.flowOn(Dispatchers.IO)
}