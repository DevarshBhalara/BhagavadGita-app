package com.example.bhagavadgitaapp.data.local


data class HomeScreenLocal(
    val slokHindi: String? = null,
    val slokEnglish: String? = null,
    var userSelectedLanguageSlok: String? = null,
    var lastReadSlokHindi: String? = null,
    var lastReadSlokEnglish: String? = null,
    var lastReadUserLanguage: String? = null,
    var chapter: Int? = null,
    var verseNumber: Int? = null
)