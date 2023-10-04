package com.example.bhagavadgitaapp.services.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "saved_slok")
data class SavedSlok (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "chapter") val chapterNumber: String,
    @ColumnInfo(name = "verse") val verseNumber: String,
    @ColumnInfo(name = "slok") val slok: String,
    @ColumnInfo(name = "hindiTranslation") val hindiTranslation: String,
    @ColumnInfo(name = "englishTranslation") val englishTranslation: String,
): Parcelable