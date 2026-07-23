package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.PrayerTime
import com.example.data.model.Surah
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onSurahClick: (Surah) -> Unit,
    onNavigateToPrayer: () -> Unit,
    onNavigateToTasbih: () -> Unit,
    onNavigateToDua: () -> Unit,
    onNavigateToLearning: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val surahs = viewModel.surahList
    val prayerTimes = viewModel.prayerTimes
    var searchFilter by remember { mutableStateOf("") }
    var selectedFilterChip by remember { mutableIntStateOf(0) } // 0: All, 1: Meccan, 2: Medinan

    val filteredSurahs = remember(searchFilter, selectedFilterChip) {
        surahs.filter { surah ->
            val matchesQuery = surah.nameEnglish.contains(searchFilter, ignoreCase = true) ||
                    surah.nameBangla.contains(searchFilter) ||
                    surah.nameArabic.contains(searchFilter) ||
                    surah.number.toString() == searchFilter

            val matchesType = when (selectedFilterChip) {
                1 -> surah.revelationType == "Meccan"
                2 -> surah.revelationType == "Medinan"
                else -> true
            }

            matchesQuery && matchesType
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = GoldSecondary,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = "App Logo",
                                    tint = EmeraldDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Sakib Muslim Quran",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "القرآن الكريم • Offline & Tajweed",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldDark
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleNightMode() },
                        modifier = Modifier.testTag("toggle_night_mode")
                    ) {
                        Icon(
                            imageVector = if (uiState.isNightReadingMode) Icons.Default.LightMode else Icons.Default.Bedtime,
                            contentDescription = "Theme Toggle",
                            tint = GoldSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Hero Card
            item {
                HeroBannerCard(
                    userLastReadSurah = uiState.userStats.lastReadSurahName,
                    userLastReadNumber = uiState.userStats.lastReadSurahNumber,
                    nextPrayer = prayerTimes.find { it.isNext } ?: prayerTimes.first(),
                    onContinueRead = {
                        val surah = surahs.find { it.number == uiState.userStats.lastReadSurahNumber } ?: surahs.first()
                        onSurahClick(surah)
                    }
                )
            }

            // Quick Access Grid Shortcuts
            item {
                QuickAccessGrid(
                    onPrayerClick = onNavigateToPrayer,
                    onTasbihClick = onNavigateToTasbih,
                    onDuaClick = onNavigateToDua,
                    onLearningClick = onNavigateToLearning
                )
            }

            // Search Bar & Filter Chips
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    OutlinedTextField(
                        value = searchFilter,
                        onValueChange = { searchFilter = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("surah_search_field"),
                        placeholder = { Text("Search Surah, Ayah or Keyword (e.g. Fatihah, 114)...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldPrimary) },
                        trailingIcon = if (searchFilter.isNotEmpty()) {
                            {
                                IconButton(onClick = { searchFilter = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        } else null,
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = selectedFilterChip == 0,
                            onClick = { selectedFilterChip = 0 },
                            label = { Text("All (114)") }
                        )
                        FilterChip(
                            selected = selectedFilterChip == 1,
                            onClick = { selectedFilterChip = 1 },
                            label = { Text("Meccan (মক্কী)") }
                        )
                        FilterChip(
                            selected = selectedFilterChip == 2,
                            onClick = { selectedFilterChip = 2 },
                            label = { Text("Medinan (মাদানী)") }
                        )
                    }
                }
            }

            // Surah List Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Surah Index (সূরা সূচী)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                    Text(
                        text = "${filteredSurahs.size} Surahs",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Surah Items
            items(filteredSurahs, key = { it.number }) { surah ->
                SurahListItem(
                    surah = surah,
                    onClick = { onSurahClick(surah) }
                )
            }
        }
    }
}

@Composable
fun HeroBannerCard(
    userLastReadSurah: String,
    userLastReadNumber: Int,
    nextPrayer: PrayerTime,
    onContinueRead: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(EmeraldDark, EmeraldPrimary, Color(0xFF135A39))
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoStories,
                            contentDescription = null,
                            tint = GoldSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Last Read • সর্বশেষ পঠিত",
                            style = MaterialTheme.typography.labelMedium,
                            color = GoldLight
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = GoldDark.copy(alpha = 0.3f)
                    ) {
                        Text(
                            text = "Juz 1",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "$userLastReadNumber. Surah $userLastReadSurah",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Continue your daily Quran journey with Tajweed & Word-by-Word",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onContinueRead,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldSecondary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("continue_reading_button")
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = EmeraldDark)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Continue Reading", color = EmeraldDark, fontWeight = FontWeight.Bold)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = GoldLight, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Next: ${nextPrayer.nameEnglish} (${nextPrayer.timeString})",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldLight
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickAccessGrid(
    onPrayerClick: () -> Unit,
    onTasbihClick: () -> Unit,
    onDuaClick: () -> Unit,
    onLearningClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        QuickShortcutItem("Prayer & Qibla", "সালাত ও কিবলা", Icons.Outlined.AccessTime, EmeraldContainer, EmeraldPrimary, onPrayerClick)
        QuickShortcutItem("Tasbih", "তাসবীহ", Icons.Outlined.Fingerprint, GoldContainer, GoldDark, onTasbihClick)
        QuickShortcutItem("Duas & Azkar", "দোআ ও জিকির", Icons.Outlined.FavoriteBorder, EmeraldContainer, EmeraldPrimary, onDuaClick)
        QuickShortcutItem("Qaida Learn", "কায়দা শিক্ষা", Icons.Outlined.School, GoldContainer, GoldDark, onLearningClick)
    }
}

@Composable
fun QuickShortcutItem(
    title: String,
    banglaTitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bgColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = bgColor,
            modifier = Modifier.size(60.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = title, tint = iconColor, modifier = Modifier.size(28.dp))
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = banglaTitle,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SurahListItem(
    surah: Surah,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() }
            .testTag("surah_item_${surah.number}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = null,
                        tint = GoldSecondary,
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        text = surah.number.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                }

                Column {
                    Text(
                        text = surah.nameEnglish,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${surah.nameBangla} • ${surah.banglaMeaning}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${surah.revelationType} • ${surah.versesCount} Verses",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldDark
                    )
                }
            }

            Text(
                text = surah.nameArabic,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary,
                fontFamily = FontFamily.Serif
            )
        }
    }
}
