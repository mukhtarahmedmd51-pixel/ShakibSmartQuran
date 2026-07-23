package com.example.data.model

data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val nameBangla: String,
    val englishMeaning: String,
    val banglaMeaning: String,
    val versesCount: Int,
    val revelationType: String, // Meccan or Medinan
    val juzNumber: Int = 1
)

data class WordDetail(
    val position: Int,
    val arabic: String,
    val transliteration: String,
    val english: String,
    val bangla: String
)

data class Ayah(
    val id: Int,
    val surahNumber: Int,
    val ayahNumber: Int,
    val textArabic: String,
    val textEnglish: String,
    val textBangla: String,
    val tafsirShort: String = "",
    val words: List<WordDetail> = emptyList(),
    val audioUrl: String = "",
    var isFavorite: Boolean = false,
    var isBookmarked: Boolean = false
)

data class PrayerTime(
    val nameEnglish: String,
    val nameBangla: String,
    val timeString: String,
    val iconName: String,
    val isNext: Boolean = false
)

data class Dua(
    val id: Int,
    val category: String,
    val titleBangla: String,
    val titleEnglish: String,
    val textArabic: String,
    val transliteration: String,
    val translationBangla: String,
    val translationEnglish: String,
    val reference: String
)

data class AllahName(
    val number: Int,
    val arabic: String,
    val transliteration: String,
    val englishMeaning: String,
    val banglaMeaning: String,
    val benefit: String
)

data class Hadith(
    val id: Int,
    val bookName: String,
    val chapter: String,
    val textArabic: String,
    val textBangla: String,
    val textEnglish: String,
    val narrator: String
)

data class Article(
    val id: Int,
    val title: String,
    val category: String,
    val author: String,
    val summary: String,
    val content: String,
    val date: String,
    val readTimeMinutes: Int
)

data class NooraniLesson(
    val id: Int,
    val lessonNumber: Int,
    val title: String,
    val description: String,
    val arabicLetters: List<String>,
    val pronunciationGuide: String
)

data class Note(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val content: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val timestamp: Long = System.currentTimeMillis()
)

data class UserReadingStat(
    val totalSurahsCompleted: Int = 3,
    val totalAyahsReadToday: Int = 24,
    val currentStreakDays: Int = 7,
    val totalMinutesListened: Int = 145,
    val lastReadSurahNumber: Int = 1,
    val lastReadAyahNumber: Int = 1,
    val lastReadSurahName: String = "Al-Fatihah",
    val lastReadTimestamp: Long = System.currentTimeMillis()
)
