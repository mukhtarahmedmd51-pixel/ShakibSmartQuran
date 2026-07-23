package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    val presetDhikrs = listOf(
        Pair("سُبْحَانَ اللَّهِ (SubhanAllah)", 33),
        Pair("الْحَمْدُ لِلَّهِ (Alhamdulillah)", 33),
        Pair("اللَّهُ أَكْبَرُ (Allahu Akbar)", 34),
        Pair("أَسْتَغْفِرُ اللَّهَ (Astaghfirullah)", 100),
        Pair("لَا إِلَهَ إِلَّا اللَّهُ (La ilaha illallah)", 100)
    )

    var isPressed by remember { mutableStateOf(false) }
    val scaleAnim by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "tasbih_press"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Digital Tasbih Counter (ডিজিটাল তাসবীহ)", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { viewModel.resetTasbih() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset Counter", tint = GoldDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Preset Dhikr Selector Row
            Column {
                Text(
                    text = "Select Dhikr • জিকির নির্বাচন করুন",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(presetDhikrs) { (dhikr, target) ->
                        FilterChip(
                            selected = uiState.tasbihDhikr == dhikr,
                            onClick = { viewModel.setTasbihDhikr(dhikr, target) },
                            label = { Text(dhikr, fontSize = 12.sp) }
                        )
                    }
                }
            }

            // Current Active Dhikr Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldDark)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.tasbihDhikr,
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = FontFamily.Serif,
                        color = GoldLight,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Target Goal: ${uiState.tasbihTarget}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            // Huge Circular Tap Button for Counting
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .scale(scaleAnim)
                    .size(240.dp)
                    .clickable {
                        isPressed = true
                        viewModel.incrementTasbih()
                        isPressed = false
                    }
                    .testTag("tasbih_counter_button")
            ) {
                Surface(
                    shape = CircleShape,
                    color = EmeraldPrimary,
                    shadowElevation = 12.dp,
                    border = BorderStroke(8.dp, GoldSecondary),
                    modifier = Modifier.fillMaxSize()
                ) {}

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${uiState.tasbihCount}",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.TouchApp, contentDescription = null, tint = GoldLight, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "TAP HERE",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldLight,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Progress Bar
            Column(modifier = Modifier.fillMaxWidth()) {
                val progress = (uiState.tasbihCount.toFloat() / uiState.tasbihTarget.toFloat()).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    color = GoldSecondary,
                    trackColor = EmeraldContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Completed: ${uiState.tasbihCount}", style = MaterialTheme.typography.labelSmall)
                    Text(text = "Goal: ${uiState.tasbihTarget}", style = MaterialTheme.typography.labelSmall, color = GoldDark)
                }
            }
        }
    }
}
