package com.example.myapplicationnew.domain.usecase


import com.example.myapplicationnew.domain.model.Anime
import com.example.myapplicationnew.domain.repository.AnimeRepository
import com.example.myapplicationnew.helper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnimeListUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    operator fun invoke(): Flow<Resource<List<Anime>>> =
        repository.getAnimeList()
}

class GetAnimeDetailUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    operator fun invoke(id: Int): Flow<Resource<Anime?>> =
        repository.getAnimeDetail(id)
}
