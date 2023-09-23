package com.example.bhagavadgitaapp.data.services

import com.example.bhagavadgitaapp.data.remote.Chapter
import com.example.bhagavadgitaapp.data.remote.Slok
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("slok/{ch}/{sl}")
    suspend fun getRandomSlok(@Path("ch") ch: Int, @Path("sl") slok: Int): Response<Slok>

    @GET("chapters")
    suspend fun getChapters(): Response<ArrayList<Chapter>>
}