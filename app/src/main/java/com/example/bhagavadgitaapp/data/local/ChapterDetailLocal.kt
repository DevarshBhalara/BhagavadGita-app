package com.example.bhagavadgitaapp.data.local

data class ChapterDetailLocal(
    val chapterNumber: Int? = null,
    val versesCount: Int? = null,
    val name: String? = null,
    val translation: String? = null,
    val transliteration: String? = null,
    val meaningHindi: String? = null,
    val meaningEnglish: String? = null,
    val summaryHindi: String? = null,
    val summaryEnglish: String? = null,
    var userSelectedName: String? = translation,
    var userSelectedLanguageMeaning: String? = summaryEnglish,
    var userSelectedLanguageSummary: String? = summaryEnglish,
)