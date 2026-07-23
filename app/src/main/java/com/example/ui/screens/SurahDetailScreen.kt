package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Ayah
import com.example.data.model.Surah
import com.example.data.model.WordDetail
import com.example.ui.MainViewModel
import com.example.ui.QuranUiState
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahDetailScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val audioState by viewModel.audioManager.audioState.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    val surah = uiState.selectedSurah ?: viewModel.surahList.first()
    val ayahs = viewModel.getAyahsForSelectedSurah()

    var showFontSettingsSheet by remember { mutableStateOf(false) }
    var selectedAyahForNote by remember { mutableStateOf<Ayah?>(null) }
    var noteTitleInput by remember { mutableStateOf("") }
    var noteContentInput by remember { mutableStateOf("") }

    var expandedWordAyahId by remember { mutableIntStateOf(-1) }
    var selectedAyahForTafsir by remember { mutableStateOf<Ayah?>(null) }

    val nightBg = if (uiState.isNightReadingMode) NightBackground else MaterialTheme.colorScheme.background

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "${surah.number}. Surah ${surah.nameEnglish}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${surah.nameBangla} • ${surah.versesCount} Ayahs",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldDark
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleWordByWord() }) {
                        Icon(
                            imageVector = Icons.Outlined.Translate,
                            contentDescription = "Word by Word",
                            tint = if (uiState.showWordByWord) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { viewModel.toggleTajweedColor() }) {
                        Icon(
                            imageVector = Icons.Outlined.FormatColorFill,
                            contentDescription = "Tajweed Color",
                            tint = if (uiState.showTajweedColor) GoldSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { showFontSettingsSheet = true }) {
                        Icon(Icons.Outlined.TextFormat, contentDescription = "Font Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            // Persistent Audio Player Bar
            AudioPlayerControlBar(
                audioState = audioState,
                surahName = surah.nameEnglish,
                onPlayPause = { viewModel.audioManager.togglePlayPause() },
                onRepeatToggle = { viewModel.audioManager.toggleRepeat() },
                onReciterChange = { viewModel.audioManager.setReciter(it) },
                reciters = viewModel.audioManager.availableReciters
            )
        },
        containerColor = nightBg
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Bismillah Header (Except Surah At-Tawbah 9)
            if (surah.number != 9) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = EmeraldDark)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = GoldLight,
                                    fontFamily = FontFamily.Serif,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "In the name of Allah, Most Gracious, Most Merciful",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }

            // Ayah List
            items(ayahs, key = { it.ayahNumber }) { ayah ->
                val isBookmarked = bookmarks.any { it.id == "${surah.number}_${ayah.ayahNumber}" }
                val isFavorite = favorites.any { it.id == "${surah.number}_${ayah.ayahNumber}" }
                val isPlayingThis = audioState.isPlaying && audioState.currentAyahNumber == ayah.ayahNumber

                AyahCardItem(
                    ayah = ayah,
                    surahName = surah.nameEnglish,
                    uiState = uiState,
                    isBookmarked = isBookmarked,
                    isFavorite = isFavorite,
                    isPlaying = isPlayingThis,
                    isWordExpanded = expandedWordAyahId == ayah.ayahNumber,
                    onPlayAudio = {
                        viewModel.audioManager.playAyah(surah.number, ayah.ayahNumber, surah.nameEnglish)
                    },
                    onBookmark = { viewModel.toggleBookmark(ayah, surah.nameEnglish) },
                    onFavorite = { viewModel.toggleFavorite(ayah, surah.nameEnglish) },
                    onToggleWordDetails = {
                        expandedWordAyahId = if (expandedWordAyahId == ayah.ayahNumber) -1 else ayah.ayahNumber
                    },
                    onOpenTafsir = { selectedAyahForTafsir = ayah },
                    onAddNote = { selectedAyahForNote = ayah }
                )
            }
        }
    }

    // Font Settings Modal Bottom Sheet
    if (showFontSettingsSheet) {
        ModalBottomSheet(onDismissRequest = { showFontSettingsSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Reading Customization",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Arabic Font Size: ${uiState.fontSizeSp} sp",
                    style = MaterialTheme.typography.bodyMedium
                )
                Slider(
                    value = uiState.fontSizeSp.toFloat(),
                    onValueChange = { viewModel.setFontSize(it.toInt()) },
                    valueRange = 18f..38f,
                    steps = 10,
                    colors = SliderDefaults.colors(thumbColor = EmeraldPrimary, activeTrackColor = EmeraldPrimary)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Translation Display Language", style = MaterialTheme.typography.bodyMedium)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Bangla", "English", "Both").forEach { lang ->
                        FilterChip(
                            selected = uiState.selectedLanguage == lang,
                            onClick = { viewModel.setLanguage(lang) },
                            label = { Text(lang) }
                        )
                    }
                }
            }
        }
    }

    // Tafsir Dialog
    selectedAyahForTafsir?.let { ayah ->
        AlertDialog(
            onDismissRequest = { selectedAyahForTafsir = null },
            title = { Text("Tafsir Preview • Surah ${surah.nameEnglish} Ayah ${ayah.ayahNumber}") },
            text = {
                Column {
                    Text(text = ayah.textArabic, fontFamily = FontFamily.Serif, fontSize = 20.sp, color = EmeraldPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = ayah.tafsirShort, style = MaterialTheme.typography.bodyMedium)
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedAyahForTafsir = null }) {
                    Text("Close")
                }
            }
        )
    }

    // Add Note Dialog
    selectedAyahForNote?.let { ayah ->
        AlertDialog(
            onDismissRequest = { selectedAyahForNote = null },
            title = { Text("Add Islamic Note for Ayah ${ayah.ayahNumber}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = noteTitleInput,
                        onValueChange = { noteTitleInput = it },
                        label = { Text("Note Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = noteContentInput,
                        onValueChange = { noteContentInput = it },
                        label = { Text("Reflection / Notes") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (noteTitleInput.isNotBlank() && noteContentInput.isNotBlank()) {
                            viewModel.addNote(noteTitleInput, noteContentInput, surah.number, ayah.ayahNumber)
                            noteTitleInput = ""
                            noteContentInput = ""
                            selectedAyahForNote = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Save Note")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedAyahForNote = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AyahCardItem(
    ayah: Ayah,
    surahName: String,
    uiState: QuranUiState,
    isBookmarked: Boolean,
    isFavorite: Boolean,
    isPlaying: Boolean,
    isWordExpanded: Boolean,
    onPlayAudio: () -> Unit,
    onBookmark: () -> Unit,
    onFavorite: () -> Unit,
    onToggleWordDetails: () -> Unit,
    onOpenTafsir: () -> Unit,
    onAddNote: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ayah_card_${ayah.ayahNumber}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlaying) GoldContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, if (isPlaying) GoldSecondary else MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Action Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = EmeraldPrimary,
                    modifier = Modifier.size(30.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${ayah.ayahNumber}",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onPlayAudio, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                            contentDescription = "Play Ayah",
                            tint = EmeraldPrimary
                        )
                    }
                    IconButton(onClick = onBookmark, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = GoldDark
                        )
                    }
                    IconButton(onClick = onFavorite, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = Color.Red
                        )
                    }
                    IconButton(onClick = onOpenTafsir, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Outlined.MenuBook, contentDescription = "Tafsir", tint = EmeraldPrimary)
                    }
                    IconButton(onClick = onAddNote, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Outlined.EditNote, contentDescription = "Note", tint = GoldDark)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Arabic Text with Tajweed or Uthmani
            val arabicAnnotatedString = remember(ayah.textArabic, uiState.showTajweedColor) {
                if (uiState.showTajweedColor) {
                    buildAnnotatedString {
                        val words = ayah.textArabic.split(" ")
                        words.forEachIndexed { index, word ->
                            val color = when (index % 4) {
                                0 -> TajweedGreen
                                1 -> TajweedBlue
                                2 -> TajweedOrange
                                else -> EmeraldPrimary
                            }
                            withStyle(SpanStyle(color = color)) {
                                append(word)
                            }
                            if (index < words.size - 1) append(" ")
                        }
                    }
                } else {
                    buildAnnotatedString { append(ayah.textArabic) }
                }
            }

            Text(
                text = arabicAnnotatedString,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = uiState.fontSizeSp.sp,
                    lineHeight = (uiState.fontSizeSp * 1.8).sp
                ),
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Translations Display
            if (uiState.selectedLanguage == "Bangla" || uiState.selectedLanguage == "Both") {
                Text(
                    text = ayah.textBangla,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (uiState.selectedLanguage == "English" || uiState.selectedLanguage == "Both") {
                Text(
                    text = ayah.textEnglish,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Word-by-Word Accordion
            if (uiState.showWordByWord && ayah.words.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = onToggleWordDetails,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = if (isWordExpanded) "Hide Word Breakdown ▲" else "Word-by-Word Breakdown ▼",
                        style = MaterialTheme.typography.labelMedium,
                        color = GoldDark
                    )
                }

                if (isWordExpanded) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ayah.words.forEach { word ->
                            WordChipItem(word)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WordChipItem(word: WordDetail) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = EmeraldContainer,
        border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = word.arabic,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Serif,
                color = EmeraldDark,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = word.transliteration,
                style = MaterialTheme.typography.labelSmall,
                color = GoldDark
            )
            Text(
                text = word.bangla,
                style = MaterialTheme.typography.labelSmall,
                color = OnEmeraldContainer
            )
        }
    }
}

@Composable
fun AudioPlayerControlBar(
    audioState: com.example.ui.audio.AudioState,
    surahName: String,
    onPlayPause: () -> Unit,
    onRepeatToggle: () -> Unit,
    onReciterChange: (String) -> Unit,
    reciters: List<String>
) {
    var showReciterDropdown by remember { mutableStateOf(false) }

    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        color = EmeraldDark
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Playing Ayah ${audioState.currentAyahNumber} • $surahName",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = audioState.reciterName,
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldLight,
                    modifier = Modifier.clickable { showReciterDropdown = true }
                )

                DropdownMenu(
                    expanded = showReciterDropdown,
                    onDismissRequest = { showReciterDropdown = false }
                ) {
                    reciters.forEach { r ->
                        DropdownMenuItem(
                            text = { Text(r) },
                            onClick = {
                                onReciterChange(r)
                                showReciterDropdown = false
                            }
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onRepeatToggle) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "Repeat",
                        tint = if (audioState.isRepeatEnabled) GoldSecondary else Color.White.copy(alpha = 0.5f)
                    )
                }

                IconButton(onClick = onPlayPause) {
                    Icon(
                        imageVector = if (audioState.isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                        contentDescription = "Play/Pause",
                        tint = GoldSecondary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}
