package com.example.bhagavadgitaapp.data.repository

import com.example.bhagavadgitaapp.utils.Resource
import com.google.gson.Gson
import retrofit2.Response

abstract class BaseRepository {

    fun <T> handleResponse(response: Response<T>): Resource<T> {
        return try {
            if (response.isSuccessful) {
                return Resource.Success(response.body())
            } else {
                if (response.code() in 400..499) {
                    response.errorBody().let {
                        return Resource.Error("Something went wrong!")
                    }
                } else {
                    return Resource.Error(response.message())
                }
            }
        } catch (e: Error) {
            Resource.Error(e.message ?: "Something went wrong!")
        }
    }
}