package com.example.myapplicationnew.data.remote

data class AnimeListResponse(val data: List<AnimeDto>)
data class AnimeDetailResponse(val data: AnimeDto)

data class AnimeDto(
    val mal_id: Int,
    val title: String,
    val images: ImagesDto?,
    val synopsis: String?,
    val score: Double?,
    val episodes: Int?,
    val genres: List<GenreDto>,
    val trailer: TrailerDto?
)

data class ImagesDto(val jpg: JpgDto)
data class JpgDto(val image_url: String?)
data class GenreDto(val name: String)
data class TrailerDto(val embed_url: String?)
