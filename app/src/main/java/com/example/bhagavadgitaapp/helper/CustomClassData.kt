package com.example.bhagavadgitaapp.helper

import android.content.Context
import com.example.bhagavadgitaapp.data.local.HomeScreenLocal
import com.example.bhagavadgitaapp.utils.AppConstants
import com.google.gson.Gson

class CustomClassData(private val context: Context) {

    private val sharedPreferences = PreferenceHelper(context)
    private val gson = Gson()

    // Save custom class data to SharedPreferences
    fun saveCustomClass(customObject: HomeScreenLocal) {
        val json = gson.toJson(customObject)
        sharedPreferences.putString(AppConstants.homeScreenLocal, json)
    }

    // Retrieve custom class data from SharedPreferences
    fun getCustomClass(): HomeScreenLocal? {
        val json = sharedPreferences.getString(AppConstants.homeScreenLocal, "")
        return gson.fromJson(json, HomeScreenLocal::class.java)
    }
}