package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NooraniLesson
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningScreen(viewModel: MainViewModel) {
    var activeSubTab by remember { mutableIntStateOf(0) } // 0: Noorani Qaida, 1: Tajweed Rules, 2: Quran Quiz

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Islamic Learning & Qaida (কায়দা ও শিক্ষা)", fontWeight = FontWeight.Bold) },
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
                Tab(
                    selected = activeSubTab == 0,
                    onClick = { activeSubTab = 0 },
                    text = { Text("Noorani Qaida") }
                )
                Tab(
                    selected = activeSubTab == 1,
                    onClick = { activeSubTab = 1 },
                    text = { Text("Tajweed Rules") }
                )
                Tab(
                    selected = activeSubTab == 2,
                    onClick = { activeSubTab = 2 },
                    text = { Text("Quran Quiz") }
                )
            }

            when (activeSubTab) {
                0 -> NooraniQaidaView(viewModel.nooraniLessons)
                1 -> TajweedRulesView()
                2 -> QuranQuizView()
            }
        }
    }
}

@Composable
fun NooraniQaidaView(lessons: List<NooraniLesson>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(lessons) { lesson ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(shape = CircleShape, color = EmeraldPrimary) {
                            Text(
                                text = "Lesson ${lesson.lessonNumber}",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = lesson.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    Text(text = lesson.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Arabic Letters Grid Display
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(6),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(lesson.arabicLetters) { letter ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = EmeraldContainer,
                                border = BorderStroke(1.dp, GoldSecondary)
                            ) {
                                Box(
                                    modifier = Modifier.padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = letter,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontFamily = FontFamily.Serif,
                                        color = EmeraldDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = GoldContainer)
                    ) {
                        Text(
                            text = "💡 ${lesson.pronunciationGuide}",
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = OnGoldContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TajweedRulesView() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(text = "Essential Tajweed Rules (তাজবীদ নিয়মাবলী)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
        }

        listOf(
            Triple("Ghunnah (গুণ্নাহ)", "Nasal sound when pronouncing Nun or Mim with Tashdeed.", TajweedRed),
            Triple("Iqlab (ইকলাব)", "Converting Tanween or Nun Sakinah to Mim sound before Ba.", TajweedPurple),
            Triple("Idgham (ইদগাম)", "Merging letters together smoothly into one doubled sound.", TajweedGreen),
            Triple("Qalqalah (ক্বলক্বলাহ)", "Bouncing or echoing sound on letters: ق, ط, ب, ج, د.", TajweedBlue)
        ).forEach { (title, desc, color) ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(2.dp, color)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = desc, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun QuranQuizView() {
    var selectedAnswerIndex by remember { mutableIntStateOf(-1) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = EmeraldDark)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Daily Quran Quiz • প্রশ্ন ১/৫", style = MaterialTheme.typography.labelSmall, color = GoldLight)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "How many Surahs are there in the Holy Quran?",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "পবিত্র কুরআনে মোট কতটি সূরা রয়েছে?", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
            }
        }

        listOf("110 Surahs", "114 Surahs", "120 Surahs", "86 Surahs").forEachIndexed { index, option ->
            OutlinedButton(
                onClick = {
                    selectedAnswerIndex = index
                    isAnswerSubmitted = true
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isAnswerSubmitted && index == 1) EmeraldContainer
                    else if (isAnswerSubmitted && selectedAnswerIndex == index && index != 1) Color(0xFFFFEBEE)
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = option, fontWeight = FontWeight.Bold)
                    if (isAnswerSubmitted && index == 1) {
                        Icon(Icons.Default.Check, contentDescription = "Correct", tint = EmeraldPrimary)
                    }
                }
            }
        }

        if (isAnswerSubmitted) {
            Text(
                text = if (selectedAnswerIndex == 1) "MashaAllah! Correct Answer 🎉" else "Incorrect. The correct answer is 114 Surahs.",
                style = MaterialTheme.typography.bodyMedium,
                color = if (selectedAnswerIndex == 1) EmeraldPrimary else Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
