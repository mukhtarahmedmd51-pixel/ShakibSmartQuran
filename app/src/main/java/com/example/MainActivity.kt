package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.Surah
import com.example.ui.MainViewModel
import com.example.ui.screens.*
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.ShakibQuranTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val uiState by mainViewModel.uiState.collectAsState()

            var activeScreen by remember { mutableStateOf<Screen>(Screen.Home) }

            ShakibQuranTheme(darkTheme = uiState.isNightReadingMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (activeScreen !is Screen.SurahDetail) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = EmeraldPrimary
                            ) {
                                NavigationBarItem(
                                    selected = activeScreen is Screen.Home,
                                    onClick = { activeScreen = Screen.Home },
                                    icon = { Icon(Icons.Outlined.MenuBook, contentDescription = "Quran") },
                                    label = { Text("Quran") },
                                    modifier = Modifier.testTag("nav_item_quran")
                                )
                                NavigationBarItem(
                                    selected = activeScreen is Screen.Prayer,
                                    onClick = { activeScreen = Screen.Prayer },
                                    icon = { Icon(Icons.Outlined.AccessTime, contentDescription = "Prayer") },
                                    label = { Text("Prayer") },
                                    modifier = Modifier.testTag("nav_item_prayer")
                                )
                                NavigationBarItem(
                                    selected = activeScreen is Screen.Tasbih,
                                    onClick = { activeScreen = Screen.Tasbih },
                                    icon = { Icon(Icons.Outlined.Fingerprint, contentDescription = "Tasbih") },
                                    label = { Text("Tasbih") },
                                    modifier = Modifier.testTag("nav_item_tasbih")
                                )
                                NavigationBarItem(
                                    selected = activeScreen is Screen.Duas,
                                    onClick = { activeScreen = Screen.Duas },
                                    icon = { Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Duas") },
                                    label = { Text("Duas") },
                                    modifier = Modifier.testTag("nav_item_duas")
                                )
                                NavigationBarItem(
                                    selected = activeScreen is Screen.Learning,
                                    onClick = { activeScreen = Screen.Learning },
                                    icon = { Icon(Icons.Outlined.School, contentDescription = "Learning") },
                                    label = { Text("Learning") },
                                    modifier = Modifier.testTag("nav_item_learning")
                                )
                                NavigationBarItem(
                                    selected = activeScreen is Screen.Personalization,
                                    onClick = { activeScreen = Screen.Personalization },
                                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Profile") },
                                    label = { Text("Profile") },
                                    modifier = Modifier.testTag("nav_item_profile")
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (val screen = activeScreen) {
                            is Screen.Home -> {
                                HomeScreen(
                                    viewModel = mainViewModel,
                                    onSurahClick = { surah ->
                                        mainViewModel.selectSurah(surah)
                                        activeScreen = Screen.SurahDetail
                                    },
                                    onNavigateToPrayer = { activeScreen = Screen.Prayer },
                                    onNavigateToTasbih = { activeScreen = Screen.Tasbih },
                                    onNavigateToDua = { activeScreen = Screen.Duas },
                                    onNavigateToLearning = { activeScreen = Screen.Learning }
                                )
                            }
                            is Screen.SurahDetail -> {
                                SurahDetailScreen(
                                    viewModel = mainViewModel,
                                    onBackClick = { activeScreen = Screen.Home }
                                )
                            }
                            is Screen.Prayer -> PrayerQiblaScreen(viewModel = mainViewModel)
                            is Screen.Tasbih -> TasbihScreen(viewModel = mainViewModel)
                            is Screen.Duas -> DuasAzkarScreen(viewModel = mainViewModel)
                            is Screen.Learning -> LearningScreen(viewModel = mainViewModel)
                            is Screen.Personalization -> PersonalizationScreen(viewModel = mainViewModel)
                        }
                    }
                }
            }
        }
    }
}

sealed class Screen {
    object Home : Screen()
    object SurahDetail : Screen()
    object Prayer : Screen()
    object Tasbih : Screen()
    object Duas : Screen()
    object Learning : Screen()
    object Personalization : Screen()
}
