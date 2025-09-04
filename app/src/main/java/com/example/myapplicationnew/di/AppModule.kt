package com.example.myapplicationnew.di


import android.content.Context
import androidx.room.Room
import com.example.myapplicationnew.data.local.AnimeDao
import com.example.myapplicationnew.data.local.AnimeDatabase
import com.example.myapplicationnew.data.local.Converters
import com.example.myapplicationnew.data.remote.AnimeApi
import com.example.myapplicationnew.data.repository.AnimeRepositoryImpl
import com.example.myapplicationnew.domain.repository.AnimeRepository
import com.example.myapplicationnew.domain.usecase.GetAnimeDetailUseCase
import com.example.myapplicationnew.domain.usecase.GetAnimeListUseCase
import com.example.myapplicationnew.helper.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ✅ Retrofit API
    @Provides
    @Singleton
    fun provideAnimeApi(): AnimeApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AnimeApi::class.java)
    }

    // ✅ Database
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AnimeDatabase {
        return Room.databaseBuilder(
            context,
            AnimeDatabase::class.java,
            "anime_db"
        )
            .addTypeConverter(Converters()) // for genres list
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideDao(db: AnimeDatabase): AnimeDao = db.animeDao()

    // ✅ Network Monitor
    @Provides
    @Singleton
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return NetworkMonitor(context)
    }

    // ✅ Repository
    @Provides
    @Singleton
    fun provideRepository(
        api: AnimeApi,
        dao: AnimeDao,
        networkMonitor: NetworkMonitor
    ): AnimeRepository = AnimeRepositoryImpl(api, dao, networkMonitor)

    // ✅ UseCases
    @Provides
    @Singleton
    fun provideGetAnimeListUseCase(repo: AnimeRepository): GetAnimeListUseCase =
        GetAnimeListUseCase(repo)

    @Provides
    @Singleton
    fun provideGetAnimeDetailUseCase(repo: AnimeRepository): GetAnimeDetailUseCase =
        GetAnimeDetailUseCase(repo)
}
