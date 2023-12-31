package com.example.bhagavadgitaapp.di

import androidx.room.Room
import com.example.bhagavadgitaapp.MyApp
import com.example.bhagavadgitaapp.ui.repository.HomeRepository
import com.example.bhagavadgitaapp.data.services.ApiService
import com.example.bhagavadgitaapp.services.room.SavedSlokDao
import com.example.bhagavadgitaapp.services.room.SlokDatabase
import com.example.bhagavadgitaapp.ui.repository.ChapterDetailRepository
import com.example.bhagavadgitaapp.ui.repository.SavedSlokRepository
import com.example.bhagavadgitaapp.ui.repository.SlokRepository
import com.example.bhagavadgitaapp.utils.AppConstants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    private val database = Room.databaseBuilder(MyApp.getAppContext(), SlokDatabase::class.java, AppConstants.database).build()

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideApiRetrofit(gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideHomeRepository(apiService: ApiService): HomeRepository =
        HomeRepository(apiService)

    @Provides
    @Singleton
    fun provideChapterDetailRepository(apiService: ApiService): ChapterDetailRepository =
        ChapterDetailRepository(apiService)

    @Provides
    @Singleton
    fun provideSlokRepository(apiService: ApiService, slokDao: SavedSlokDao): SlokRepository =
        SlokRepository(apiService, slokDao)

    @Provides
    @Singleton
    fun provideSavedSlokDaoService(): SavedSlokDao =
        database.savedSlok()

    @Provides
    @Singleton
    fun provideSavedSlokRepository(slokDao: SavedSlokDao): SavedSlokRepository =
        SavedSlokRepository(slokDao)

}