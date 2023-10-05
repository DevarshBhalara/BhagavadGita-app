package com.example.bhagavadgitaapp.data.services

import com.example.bhagavadgitaapp.data.remote.Chapter
import com.example.bhagavadgitaapp.data.remote.ChapterDetail
import com.example.bhagavadgitaapp.data.remote.Slok
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("slok/{ch}/{sl}")
    suspend fun getSlok(@Path("ch") ch: Int, @Path("sl") slok: Int): Response<Slok>

    @GET("chapters")
    suspend fun getChapters(): Response<ArrayList<Chapter>>

    @GET("chapter/{ch}")
    suspend fun getParticularChapter(@Path("ch") ch: String): Response<ChapterDetail>

    @GET("slok/{ch}/{sl}/gita.svg")
    suspend fun getVerseImage(@Path("ch") ch: String, @Path("sl") slok: String): Response<String>

}