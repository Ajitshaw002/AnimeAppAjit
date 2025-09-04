package com.example.myapplicationnew.data.mapper

import com.example.myapplicationnew.data.local.AnimeEntity
import com.example.myapplicationnew.data.remote.AnimeDto
import com.example.myapplicationnew.domain.model.Anime

fun AnimeEntity.toDomain() = Anime(
    malId, title, imageUrl, synopsis, score, episodes, genres, trailerEmbedUrl
)


fun AnimeDto.toEntity() = AnimeEntity(
    malId = mal_id,
    title = title,
    imageUrl = images?.jpg?.image_url,
    synopsis = synopsis,
    score = score,
    episodes = episodes,
    genres = genres.map { it.name },
    trailerEmbedUrl = trailer?.embed_url
)
