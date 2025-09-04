package com.example.myapplicationnew.domain.model


data class Anime(
    val malId: Int,
    val title: String,
    val imageUrl: String?,
    val synopsis: String?,
    val score: Double?,
    val episodes: Int?,
    val genres: List<String>,
    val trailerEmbedUrl: String?
)
