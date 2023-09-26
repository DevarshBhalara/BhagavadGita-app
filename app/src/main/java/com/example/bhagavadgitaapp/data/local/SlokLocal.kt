package com.example.bhagavadgitaapp.data.local

data class SlokLocal(

    val id: String? = null,
    val chapter: Int? = null,
    val verse: Int? = null,
    val slok: String? = null,
    val transliteration: String? = null,
    val authors: MutableList<Authors>,
)

data class Authors (
    val authorName: String,
    val hindiTranslation: String? = null,
    val hindiCommentary: String? = null,
    val englishTranslation: String? = null,
    val englishCommentary: String? = null,
    val sanskritCommentary: String? = null,
    val totalCommentaryTranslation: Int? = null,
    var isExpanded: Boolean = false,
    val translation:  MutableList<TranslationOrCommentary>,
)

data class TranslationOrCommentary(
    val title: String,
    val content: String?
)