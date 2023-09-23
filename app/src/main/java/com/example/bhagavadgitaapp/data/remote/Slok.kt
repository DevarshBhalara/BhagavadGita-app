package com.example.bhagavadgitaapp.data.remote

import com.google.gson.annotations.SerializedName

data class Slok (

    @SerializedName("_id") val Id: String? = null,
    @SerializedName("chapter") val chapter: Int? = null,
    @SerializedName("verse") val verse: Int? = null,
    @SerializedName("slok") val slok: String? = null,
    @SerializedName("transliteration") val transliteration: String? = null,
    @SerializedName("tej") val tej: Tej? = Tej(),
    @SerializedName("siva") val siva: Siva? = Siva(),
    @SerializedName("purohit") val purohit: Purohit? = Purohit(),
    @SerializedName("chinmay") val chinmay: Chinmay? = Chinmay(),
    @SerializedName("san") val san: San? = San(),
    @SerializedName("adi") val adi: Adi? = Adi(),
    @SerializedName("gambir") val gambir: Gambir? = Gambir(),
    @SerializedName("madhav") val madhav: Madhav? = Madhav(),
    @SerializedName("anand") val anand: Anand? = Anand(),
    @SerializedName("rams") val rams: Rams? = Rams(),
    @SerializedName("raman") val raman: Raman? = Raman(),
    @SerializedName("abhinav") val abhinav: Abhinav? = Abhinav(),
    @SerializedName("sankar") val sankar: Sankar? = Sankar(),
    @SerializedName("jaya") val jaya: Jaya? = Jaya(),
    @SerializedName("vallabh") val vallabh: Vallabh? = Vallabh(),
    @SerializedName("ms") val ms: Ms? = Ms(),
    @SerializedName("srid") val srid: Srid? = Srid(),
    @SerializedName("dhan") val dhan: Dhan? = Dhan(),
    @SerializedName("venkat") val venkat: Venkat? = Venkat(),
    @SerializedName("puru") val puru: Puru? = Puru(),
    @SerializedName("neel") val neel: Neel? = Neel()
)

data class Tej(

    @SerializedName("author") val author: String? = null,
    @SerializedName("ht") val ht: String? = null

)

data class Purohit(

    @SerializedName("author") val author: String? = null,
    @SerializedName("et") val et: String? = null

)

data class Siva(

    @SerializedName("author") var author: String? = null,
    @SerializedName("et") var et: String? = null,
    @SerializedName("ec") var ec: String? = null

)

data class Chinmay(

    @SerializedName("author") val author: String? = null,
    @SerializedName("hc") val hc: String? = null

)

data class San(

    @SerializedName("author") val author: String? = null,
    @SerializedName("et") val et: String? = null

)

data class Adi(

    @SerializedName("author") val author: String? = null,
    @SerializedName("et") val et: String? = null

)

data class Gambir(

    @SerializedName("author") val author: String? = null,
    @SerializedName("et") val et: String? = null

)

data class Madhav(

    @SerializedName("author") val author: String? = null,
    @SerializedName("sc") val sc: String? = null

)

data class Anand(

    @SerializedName("author") val author: String? = null,
    @SerializedName("sc") val sc: String? = null

)

data class Rams(

    @SerializedName("author") val author: String? = null,
    @SerializedName("ht") val ht: String? = null,
    @SerializedName("hc") val hc: String? = null

)


data class Raman(

    @SerializedName("author") val author: String? = null,
    @SerializedName("sc") val sc: String? = null,
    @SerializedName("et") val et: String? = null

)

data class Abhinav(

    @SerializedName("author") val author: String? = null,
    @SerializedName("sc") val sc: String? = null,
    @SerializedName("et") val et: String? = null

)

data class Sankar(

    @SerializedName("author") val author: String? = null,
    @SerializedName("ht") val ht: String? = null,
    @SerializedName("sc") val sc: String? = null,
    @SerializedName("et") val et: String? = null

)

data class Jaya(

    @SerializedName("author") val author: String? = null,
    @SerializedName("sc") val sc: String? = null

)

data class Vallabh(

    @SerializedName("author") val author: String? = null,
    @SerializedName("sc") val sc: String? = null

)

data class Ms(

    @SerializedName("author") val author: String? = null,
    @SerializedName("sc") val sc: String? = null

)

data class Srid(

    @SerializedName("author") val author: String? = null,
    @SerializedName("sc") val sc: String? = null

)

data class Dhan(

    @SerializedName("author") val author: String? = null,
    @SerializedName("sc") val sc: String? = null

)

data class Venkat(

    @SerializedName("author") val author: String? = null,
    @SerializedName("sc") val sc: String? = null

)

data class Puru(

    @SerializedName("author") val author: String? = null,
    @SerializedName("sc") val sc: String? = null

)

data class Neel(

    @SerializedName("author") val author: String? = null,
    @SerializedName("sc") val sc: String? = null

)