package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PrayerTime
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerQiblaScreen(viewModel: MainViewModel) {
    val prayerTimes = viewModel.prayerTimes
    var qiblaAngle by remember { mutableFloatStateOf(245f) } // Default compass direction for Mecca from S. Asia
    var activeSubTab by remember { mutableIntStateOf(0) } // 0: Prayer Times, 1: Qibla Compass, 2: Hijri Calendar

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prayer Times & Qibla (সালাত ও কিবলা)", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tab Header
            TabRow(
                selectedTabIndex = activeSubTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = EmeraldPrimary
            ) {
                Tab(
                    selected = activeSubTab == 0,
                    onClick = { activeSubTab = 0 },
                    text = { Text("Prayer Times") },
                    icon = { Icon(Icons.Outlined.AccessTime, contentDescription = null) }
                )
                Tab(
                    selected = activeSubTab == 1,
                    onClick = { activeSubTab = 1 },
                    text = { Text("Qibla Compass") },
                    icon = { Icon(Icons.Outlined.Explore, contentDescription = null) }
                )
                Tab(
                    selected = activeSubTab == 2,
                    onClick = { activeSubTab = 2 },
                    text = { Text("Hijri Calendar") },
                    icon = { Icon(Icons.Outlined.CalendarToday, contentDescription = null) }
                )
            }

            when (activeSubTab) {
                0 -> PrayerTimesView(prayerTimes)
                1 -> QiblaCompassView(qiblaAngle)
                2 -> HijriCalendarView()
            }
        }
    }
}

@Composable
fun PrayerTimesView(prayerTimes: List<PrayerTime>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // Today Ramadan / Sehri & Iftar Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldPrimary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Today's Fasting Schedule",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Surface(shape = CircleShape, color = GoldSecondary) {
                            Text(
                                text = "14 Safari 1448 AH",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = EmeraldDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Sehri Ends", style = MaterialTheme.typography.labelSmall, color = GoldLight)
                            Text(text = "04:15 AM", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Divider(modifier = Modifier.height(30.dp).width(1.dp), color = GoldSecondary)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Iftar Time", style = MaterialTheme.typography.labelSmall, color = GoldLight)
                            Text(text = "06:48 PM", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Daily Prayer Schedule (দৈনিক সালাতের সময়সূচী)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        items(prayerTimes) { prayer ->
            PrayerTimeRowCard(prayer)
        }
    }
}

@Composable
fun PrayerTimeRowCard(prayer: PrayerTime) {
    var isAzanEnabled by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (prayer.isNext) GoldContainer else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, if (prayer.isNext) GoldSecondary else MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = when (prayer.nameEnglish) {
                        "Fajr" -> Icons.Default.WbTwilight
                        "Sunrise" -> Icons.Default.WbSunny
                        "Dhuhr" -> Icons.Default.WbSunny
                        "Asr" -> Icons.Default.WbCloudy
                        "Maghrib" -> Icons.Default.NightsStay
                        else -> Icons.Default.Bedtime
                    },
                    contentDescription = null,
                    tint = if (prayer.isNext) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Column {
                    Text(
                        text = "${prayer.nameEnglish} (${prayer.nameBangla})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (prayer.isNext) FontWeight.Bold else FontWeight.Medium
                    )
                    if (prayer.isNext) {
                        Text(text = "Next Prayer • পরবর্তী সালাত", style = MaterialTheme.typography.labelSmall, color = GoldDark)
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = prayer.timeString,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { isAzanEnabled = !isAzanEnabled }) {
                    Icon(
                        imageVector = if (isAzanEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                        contentDescription = "Azan Notification",
                        tint = if (isAzanEnabled) GoldSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun QiblaCompassView(qiblaDegree: Float) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Qibla Direction (কিবলা দিগদর্শন)",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary
        )
        Text(
            text = "Facing $qiblaDegree° SW towards Kaaba, Mecca Al-Mukarramah",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Graphical Compass Dial
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(280.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = EmeraldContainer,
                border = BorderStroke(4.dp, GoldSecondary),
                modifier = Modifier.fillMaxSize()
            ) {}

            // Rotating Compass Needle pointing towards Qibla
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .rotate(qiblaDegree)
            ) {
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = "Qibla Pointer",
                    tint = EmeraldPrimary,
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.TopCenter)
                )
            }

            // Central Kaaba Icon Representation
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.Black,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "🕋", fontSize = 24.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GoldContainer)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.GpsFixed, contentDescription = null, tint = GoldDark)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "GPS Location calibrated. Place device on a flat surface away from magnetic fields.",
                    style = MaterialTheme.typography.labelSmall,
                    color = OnGoldContainer
                )
            }
        }
    }
}

@Composable
fun HijriCalendarView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = EmeraldDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Safar 1448 AH", style = MaterialTheme.typography.headlineSmall, color = GoldLight, fontWeight = FontWeight.Bold)
                Text(text = "July / August 2026", style = MaterialTheme.typography.bodySmall, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Upcoming Islamic Events", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
        Spacer(modifier = Modifier.height(8.dp))

        listOf(
            "12 Rabi' al-Awwal" to "Eid-e-Miladunnabi (S.)",
            "27 Rajab" to "Shab-e-Meraj",
            "15 Sha'ban" to "Shab-e-Barat",
            "1 Ramadan" to "Start of Holy Ramadan",
            "27 Ramadan" to "Shab-e-Qadr",
            "1 Shawwal" to "Holy Eid-ul-Fitr"
        ).forEach { (date, event) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = event, fontWeight = FontWeight.Bold)
                    Text(text = date, color = GoldDark, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
