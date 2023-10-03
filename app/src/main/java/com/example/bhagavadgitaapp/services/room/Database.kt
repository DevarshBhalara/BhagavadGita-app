package com.example.bhagavadgitaapp.services.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bhagavadgitaapp.ui.activity.MainActivity
import com.example.bhagavadgitaapp.utils.AppConstants
import kotlinx.coroutines.CoroutineScope

@Database(entities = [SavedSlok::class], version = 1)
abstract class SlokDatabase: RoomDatabase() {
    abstract fun savedSlok(): SavedSlokDao

    companion object {

        @Volatile
        private var INSTANCE: SlokDatabase? = null

        fun getDatabase(ctx: MainActivity, scope: CoroutineScope): SlokDatabase {
            return when (val temp = INSTANCE) {
                null -> synchronized(this) {
                    Room.databaseBuilder(
                        ctx.applicationContext, SlokDatabase::class.java,
                        AppConstants.database
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                else -> temp
            }
        }
    }
}