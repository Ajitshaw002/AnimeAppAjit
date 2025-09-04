package com.example.myapplicationnew.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import javax.inject.Inject

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey val malId: Int,
    val title: String,
    val imageUrl: String?,
    val synopsis: String?,
    val score: Double?,
    val episodes: Int?,
    val genres: List<String>,
    val trailerEmbedUrl: String?
)

@ProvidedTypeConverter
class Converters @Inject constructor() {
    @TypeConverter
    fun fromStringList(value: List<String>?): String = value?.joinToString(",") ?: ""

    @TypeConverter
    fun toStringList(value: String): List<String> = if (value.isEmpty()) emptyList() else value.split(",")
}

