package com.example.myapplicationnew.data.repository


import com.example.myapplicationnew.data.local.AnimeDao
import com.example.myapplicationnew.data.mapper.toDomain
import com.example.myapplicationnew.data.mapper.toEntity
import com.example.myapplicationnew.data.remote.AnimeApi
import com.example.myapplicationnew.domain.model.Anime
import com.example.myapplicationnew.domain.repository.AnimeRepository
import com.example.myapplicationnew.helper.NetworkMonitor
import com.example.myapplicationnew.helper.Resource
import com.example.myapplicationnew.helper.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AnimeRepositoryImpl @Inject constructor(
    private val api: AnimeApi,
    private val dao: AnimeDao,
    private val networkMonitor: NetworkMonitor
) : AnimeRepository {

    override fun getAnimeList(): Flow<Resource<List<Anime>>> =
        networkBoundResource(
            networkMonitor = networkMonitor,
            query = { dao.getAllAnime().map { list -> list.map { it.toDomain() } } },
            fetch = { api.getAnimeList().data.map { it.toEntity() } },
            saveFetchResult = { dao.insertAll(it) }
        )

    override fun getAnimeDetail(id: Int): Flow<Resource<Anime?>> =
        networkBoundResource(
            networkMonitor = networkMonitor, // pass your monitor here
            query = { dao.getAnimeById(id).map { it?.toDomain() } },
            fetch = { api.getAnimeDetail(id).data.toEntity() },
            saveFetchResult = { dao.insert(it) },
            shouldFetch = { cached ->
                cached == null || networkMonitor.isConnected() // fetch if no cached data or network is available
            }
        )

}

