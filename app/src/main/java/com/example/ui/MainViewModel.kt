package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.BookmarkEntity
import com.example.data.db.FavoriteEntity
import com.example.data.db.NoteEntity
import com.example.data.db.TasbihEntity
import com.example.data.model.*
import com.example.data.repository.QuranRepository
import com.example.ui.audio.AudioPlayerManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class QuranUiState(
    val searchQuery: String = "",
    val selectedLanguage: String = "Bangla", // Bangla, English, Both
    val fontSizeSp: Int = 22,
    val arabicFontFamily: String = "Uthmani", // Uthmani, Indopak, Scheherazade
    val showWordByWord: Boolean = true,
    val showTajweedColor: Boolean = true,
    val isNightReadingMode: Boolean = false,
    val selectedSurah: Surah? = null,
    val activeTab: Int = 0, // 0: Quran, 1: Search, 2: Prayer & Qibla, 3: Duas & Azkar, 4: Learning, 5: More/Admin
    val selectedReciter: String = "Mishary Rashid Alafasy",
    val userStats: UserReadingStat = UserReadingStat(),
    val notesList: List<NoteEntity> = emptyList(),
    val tasbihCount: Int = 0,
    val tasbihDhikr: String = "سُبْحَانَ اللَّهِ (SubhanAllah)",
    val tasbihTarget: Int = 33
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuranRepository()
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.quranDao()

    val audioManager = AudioPlayerManager(application)

    private val _uiState = MutableStateFlow(QuranUiState())
    val uiState: StateFlow<QuranUiState> = _uiState.asStateFlow()

    val surahList: List<Surah> = repository.getSurahList()
    val prayerTimes: List<PrayerTime> = repository.getPrayerTimes()
    val allahNames: List<AllahName> = repository.getAllahNames()
    val dailyDuas: List<Dua> = repository.getDailyDuas()
    val hadiths: List<Hadith> = repository.getHadiths()
    val nooraniLessons: List<NooraniLesson> = repository.getNooraniLessons()
    val articles: List<Article> = repository.getArticles()

    val bookmarks: StateFlow<List<BookmarkEntity>> = dao.getAllBookmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<FavoriteEntity>> = dao.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notes: StateFlow<List<NoteEntity>> = dao.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Default select Al-Fatihah
        _uiState.value = _uiState.value.copy(selectedSurah = surahList.first())
    }

    fun selectSurah(surah: Surah) {
        _uiState.value = _uiState.value.copy(
            selectedSurah = surah,
            userStats = _uiState.value.userStats.copy(
                lastReadSurahNumber = surah.number,
                lastReadSurahName = surah.nameEnglish
            )
        )
    }

    fun getAyahsForSelectedSurah(): List<Ayah> {
        val currentSurah = _uiState.value.selectedSurah ?: surahList.first()
        return repository.getAyahsForSelectedSurah(currentSurah.number)
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun setTab(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(activeTab = tabIndex)
    }

    fun setFontSize(size: Int) {
        _uiState.value = _uiState.value.copy(fontSizeSp = size.coerceIn(16, 40))
    }

    fun toggleWordByWord() {
        _uiState.value = _uiState.value.copy(showWordByWord = !_uiState.value.showWordByWord)
    }

    fun toggleTajweedColor() {
        _uiState.value = _uiState.value.copy(showTajweedColor = !_uiState.value.showTajweedColor)
    }

    fun toggleNightMode() {
        _uiState.value = _uiState.value.copy(isNightReadingMode = !_uiState.value.isNightReadingMode)
    }

    fun setLanguage(lang: String) {
        _uiState.value = _uiState.value.copy(selectedLanguage = lang)
    }

    fun setArabicFont(font: String) {
        _uiState.value = _uiState.value.copy(arabicFontFamily = font)
    }

    // Bookmark operations
    fun toggleBookmark(ayah: Ayah, surahName: String) {
        viewModelScope.launch {
            val id = "${ayah.surahNumber}_${ayah.ayahNumber}"
            val existing = bookmarks.value.find { it.id == id }
            if (existing != null) {
                dao.deleteBookmark(id)
            } else {
                dao.insertBookmark(
                    BookmarkEntity(
                        id = id,
                        surahNumber = ayah.surahNumber,
                        ayahNumber = ayah.ayahNumber,
                        surahName = surahName,
                        textArabic = ayah.textArabic,
                        textBangla = ayah.textBangla
                    )
                )
            }
        }
    }

    // Favorite operations
    fun toggleFavorite(ayah: Ayah, surahName: String) {
        viewModelScope.launch {
            val id = "${ayah.surahNumber}_${ayah.ayahNumber}"
            val existing = favorites.value.find { it.id == id }
            if (existing != null) {
                dao.deleteFavorite(id)
            } else {
                dao.insertFavorite(
                    FavoriteEntity(
                        id = id,
                        surahNumber = ayah.surahNumber,
                        ayahNumber = ayah.ayahNumber,
                        surahName = surahName,
                        textArabic = ayah.textArabic,
                        textBangla = ayah.textBangla
                    )
                )
            }
        }
    }

    // Tasbih Counter
    fun incrementTasbih() {
        val current = _uiState.value.tasbihCount
        val newCount = current + 1
        _uiState.value = _uiState.value.copy(tasbihCount = newCount)
        viewModelScope.launch {
            dao.insertTasbih(
                TasbihEntity(
                    dhikrName = _uiState.value.tasbihDhikr,
                    count = newCount,
                    targetGoal = _uiState.value.tasbihTarget
                )
            )
        }
    }

    fun resetTasbih() {
        _uiState.value = _uiState.value.copy(tasbihCount = 0)
    }

    fun setTasbihDhikr(dhikr: String, target: Int) {
        _uiState.value = _uiState.value.copy(
            tasbihDhikr = dhikr,
            tasbihTarget = target,
            tasbihCount = 0
        )
    }

    // Notes
    fun addNote(title: String, content: String, surahNumber: Int, ayahNumber: Int) {
        viewModelScope.launch {
            dao.insertNote(
                NoteEntity(
                    title = title,
                    content = content,
                    surahNumber = surahNumber,
                    ayahNumber = ayahNumber
                )
            )
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            dao.deleteNote(id)
        }
    }

    // Search filter helper
    fun searchSurahsAndAyahs(): Pair<List<Surah>, List<Ayah>> {
        val q = _uiState.value.searchQuery.trim().lowercase()
        if (q.isEmpty()) return Pair(surahList, emptyList())

        val matchedSurahs = surahList.filter {
            it.nameEnglish.lowercase().contains(q) ||
            it.nameBangla.contains(q) ||
            it.nameArabic.contains(q) ||
            it.number.toString() == q
        }

        val allAyahs = surahList.flatMap { repository.getAyahsForSelectedSurah(it.number) }
        val matchedAyahs = allAyahs.filter {
            it.textEnglish.lowercase().contains(q) ||
            it.textBangla.contains(q) ||
            it.textArabic.contains(q)
        }

        return Pair(matchedSurahs, matchedAyahs)
    }
}

private fun QuranRepository.getAyahsForSelectedSurah(surahNumber: Int): List<Ayah> {
    return getAyahsForSurah(surahNumber)
}
