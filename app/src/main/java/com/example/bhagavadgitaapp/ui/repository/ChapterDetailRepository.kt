package com.example.bhagavadgitaapp.ui.repository

import android.util.Log
import com.example.bhagavadgitaapp.data.remote.ChapterDetail
import com.example.bhagavadgitaapp.data.services.ApiService
import com.example.bhagavadgitaapp.utils.Resource
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class ChapterDetailRepository(private val apiService: ApiService) : BaseRepository() {

    suspend fun getChapterDetails(chapterNumber: String) = flow<Resource<ChapterDetail>> {
        emit(Resource.Loading())
        try {
            apiService.getParticularChapter(chapterNumber).let { response ->
                val resource = handleResponse(response)
                emit(resource)
            }
        } catch (e: Exception) {
            Log.e("response", e.localizedMessage ?: "Error Occurred")
        }
    }
}