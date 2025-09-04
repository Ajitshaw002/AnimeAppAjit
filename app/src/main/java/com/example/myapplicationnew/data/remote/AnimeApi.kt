package com.example.myapplicationnew.data.remote


import retrofit2.http.GET
import retrofit2.http.Path


interface AnimeApi {
    @GET("top/anime")
    suspend fun getAnimeList(): AnimeListResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetail(@Path("id") id: Int): AnimeDetailResponse
}
