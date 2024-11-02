package com.alexvolkov.dazntestapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF171717),
    onPrimary = Color(0xFFC7C7C7),
    onSurface = Color(0xFFC4C4C4),
    background = Color(0xFF2B2B2B),
    primaryContainer = Color(0xFF1A1A1A),
    onBackground = Color(0xFFF5F5F5),
    surface = Color(0xFF404040),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF171717),
    onPrimary = Color(0xFFEAEAEA),
    onSurface = Color(0xFF323232),
    background = Color(0xFFDEDEDE),
    onBackground = Color(0xFF1A1A1A),
    primaryContainer = Color(0xFF1A1A1A),
    surface = Color(0xFFFAFAFA),
)

@Composable
fun DaznTestAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

   /* val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }*/

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}