package com.example.bhagavadgitaapp.data.remote

import com.google.gson.annotations.SerializedName

data class Chapter(

    @SerializedName("chapter_number") val chapterNumber: Int? = null,
    @SerializedName("verses_count") val versesCount: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("translation") val translation: String? = null,
    @SerializedName("transliteration") val transliteration: String? = null,
    @SerializedName("meaning") val meaning: Meaning? = Meaning(),
    @SerializedName("summary") val summary: Summary? = Summary()
)

data class Meaning(
    @SerializedName("en") val en: String? = null,
    @SerializedName("hi") val hi: String? = null
)

data class Summary(
    @SerializedName("en") val en: String? = null,
    @SerializedName("hi") val hi: String? = null
)