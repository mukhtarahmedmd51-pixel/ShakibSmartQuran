package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: String, // "surah_ayah" e.g. "1_1"
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahName: String,
    val textArabic: String,
    val textBangla: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String, // "surah_ayah" e.g. "1_1"
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahName: String,
    val textArabic: String,
    val textBangla: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasbih")
data class TasbihEntity(
    @PrimaryKey val dhikrName: String,
    val count: Int,
    val targetGoal: Int = 33,
    val lastUpdated: Long = System.currentTimeMillis()
)
