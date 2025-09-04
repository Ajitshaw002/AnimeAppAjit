package com.example.myapplicationnew.domain.repository


import com.example.myapplicationnew.domain.model.Anime
import com.example.myapplicationnew.helper.Resource
import kotlinx.coroutines.flow.Flow



interface AnimeRepository {
    fun getAnimeList(): Flow<Resource<List<Anime>>>
    fun getAnimeDetail(id: Int): Flow<Resource<Anime?>>
}
