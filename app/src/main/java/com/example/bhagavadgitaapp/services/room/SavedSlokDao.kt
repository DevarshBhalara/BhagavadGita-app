package com.example.bhagavadgitaapp.services.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SavedSlokDao {

    @Insert
    suspend fun insertAll(vararg slok: SavedSlok): List<Long>

    @Query("select * from saved_slok order by id desc")
    suspend fun getAll(): List<SavedSlok>

    @Delete
    suspend fun removeSaved(slok: SavedSlok): Int

    @Query("select * from saved_slok where chapter = :chapter AND verse = :verse")
    suspend fun isSlokSaved(chapter: String, verse: String): List<SavedSlok>
}