package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.BookmarkEntity
import com.example.data.db.FavoriteEntity
import com.example.data.db.NoteEntity
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizationScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val notes by viewModel.notes.collectAsState()

    var activeSubTab by remember { mutableIntStateOf(0) } // 0: Profile & Stats, 1: Bookmarks & Favorites, 2: Notes, 3: Admin Panel

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile & Personalization (প্রোফাইল)", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(
                selectedTabIndex = activeSubTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = EmeraldPrimary
            ) {
                Tab(selected = activeSubTab == 0, onClick = { activeSubTab = 0 }, text = { Text("Stats") })
                Tab(selected = activeSubTab == 1, onClick = { activeSubTab = 1 }, text = { Text("Saved (${bookmarks.size + favorites.size})") })
                Tab(selected = activeSubTab == 2, onClick = { activeSubTab = 2 }, text = { Text("Notes (${notes.size})") })
                Tab(selected = activeSubTab == 3, onClick = { activeSubTab = 3 }, text = { Text("Admin") })
            }

            when (activeSubTab) {
                0 -> ProfileStatsView(uiState.userStats)
                1 -> SavedCollectionCardView(bookmarks, favorites)
                2 -> NotesListView(notes, onDelete = { viewModel.deleteNote(it) })
                3 -> AdminPanelView()
            }
        }
    }
}

@Composable
fun ProfileStatsView(stats: com.example.data.model.UserReadingStat) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldDark)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(shape = CircleShape, color = GoldSecondary, modifier = Modifier.size(60.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "S", style = MaterialTheme.typography.headlineMedium, color = EmeraldDark, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "Sakib Ahmed", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(text = "sakib.quran@example.com", style = MaterialTheme.typography.bodySmall, color = GoldLight)
                        Text(text = "Sync Status: Backup Active ☁️", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
        }

        item {
            Text(text = "Reading Statistics (পঠন পরিসংখ্যান)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatMetricCard("Daily Streak", "${stats.currentStreakDays} Days", Icons.Default.LocalFireDepartment, GoldContainer, GoldDark, Modifier.weight(1f))
                StatMetricCard("Ayahs Today", "${stats.totalAyahsReadToday}", Icons.Default.MenuBook, EmeraldContainer, EmeraldPrimary, Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatMetricCard("Completed", "${stats.totalSurahsCompleted} Surahs", Icons.Default.CheckCircle, GoldContainer, GoldDark, Modifier.weight(1f))
                StatMetricCard("Listened", "${stats.totalMinutesListened} Mins", Icons.Default.Headset, EmeraldContainer, EmeraldPrimary, Modifier.weight(1f))
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Achievement Badges", fontWeight = FontWeight.Bold)
                        Text(text = "3 Unlocked", color = GoldDark, style = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(text = "🏆 7-Day Streak")
                        Text(text = "📖 Hafiz Level 1")
                        Text(text = "🎧 Devoted Reciter")
                    }
                }
            }
        }
    }
}

@Composable
fun StatMetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bgColor: Color,
    tintColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = tintColor)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = tintColor)
            Text(text = title, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun SavedCollectionCardView(bookmarks: List<BookmarkEntity>, favorites: List<FavoriteEntity>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(text = "Bookmarked Ayahs (${bookmarks.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
        }
        items(bookmarks) { b ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Surah ${b.surahName} • Ayah ${b.ayahNumber}", fontWeight = FontWeight.Bold, color = GoldDark)
                    Text(text = b.textArabic, fontSize = 18.sp, color = EmeraldPrimary)
                    Text(text = b.textBangla, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Favorite Ayahs (${favorites.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
        }
        items(favorites) { f ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Surah ${f.surahName} • Ayah ${f.ayahNumber}", fontWeight = FontWeight.Bold, color = GoldDark)
                    Text(text = f.textArabic, fontSize = 18.sp, color = EmeraldPrimary)
                    Text(text = f.textBangla, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun NotesListView(notes: List<NoteEntity>, onDelete: (Long) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (notes.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No notes saved yet. Add notes directly from Ayah reading cards!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            items(notes) { note ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = note.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                            IconButton(onClick = { onDelete(note.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                        Text(text = "Surah ${note.surahNumber} Ayah ${note.ayahNumber}", style = MaterialTheme.typography.labelSmall, color = GoldDark)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminPanelView() {
    var notificationInput by remember { mutableStateOf("") }
    var showSentToast by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldDark)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Content Management System (CMS) & Admin Panel", style = MaterialTheme.typography.titleMedium, color = GoldLight, fontWeight = FontWeight.Bold)
                    Text(text = "Manage Audio Recitations, Tafsir Uploads & Push Notifications", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Broadcast Push Notification", fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = notificationInput,
                        onValueChange = { notificationInput = it },
                        placeholder = { Text("e.g. Ramadan Mubarak! Read Surah Yaseen today...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (notificationInput.isNotBlank()) {
                                showSentToast = true
                                notificationInput = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Send Broadcast")
                    }

                    if (showSentToast) {
                        Text(text = "✅ Notification broadcasted to all active devices!", color = EmeraldPrimary, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Upload Audio / Tafsir Assets", fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Upload Custom Audio Reciter Stream")
                    }
                }
            }
        }
    }
}
