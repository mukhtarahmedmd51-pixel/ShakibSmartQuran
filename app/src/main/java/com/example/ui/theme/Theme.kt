package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = EmeraldContainer,
    onPrimaryContainer = OnEmeraldContainer,
    secondary = GoldSecondary,
    onSecondary = Color.Black,
    secondaryContainer = GoldContainer,
    onSecondaryContainer = OnGoldContainer,
    tertiary = EmeraldLight,
    background = WarmCreamBackground,
    surface = WarmSurface,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFFEFF5F1),
    onSurfaceVariant = TextSecondaryDark,
    outline = Color(0xFFC2D6CB)
)

private val DarkColorScheme = darkColorScheme(
    primary = GoldSecondary,
    onPrimary = Color.Black,
    primaryContainer = NightSurfaceVariant,
    onPrimaryContainer = GoldLight,
    secondary = EmeraldLight,
    onSecondary = Color.Black,
    secondaryContainer = EmeraldDark,
    onSecondaryContainer = EmeraldContainer,
    tertiary = GoldLight,
    background = NightBackground,
    surface = NightSurface,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = NightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFF2E453B)
)

@Composable
fun ShakibQuranTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to maintain our custom Islamic Green & Gold branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
