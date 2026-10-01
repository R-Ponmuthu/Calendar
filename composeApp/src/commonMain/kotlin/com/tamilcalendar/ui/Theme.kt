package com.tamilcalendar.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Saffron = Color(0xFFB4410E)
val Maroon = Color(0xFF7A1F1F)
val Gold = Color(0xFFC8962E)
val HolidayRed = Color(0xFFC62828)
val MuhurthamGreen = Color(0xFF2E7D32)
val KarinalGrey = Color(0xFF616161)

private val LightColors = lightColorScheme(
    primary = Maroon,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0CC),
    onPrimaryContainer = Color(0xFF3B0D00),
    secondary = Saffron,
    secondaryContainer = Color(0xFFFFF1D6),
    onSecondaryContainer = Color(0xFF3A2400),
    tertiary = Gold,
    background = Color(0xFFFFFBF5),
    surface = Color(0xFFFFFBF5),
    surfaceVariant = Color(0xFFF6E9DD),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFB59A),
    onPrimary = Color(0xFF5A1A00),
    primaryContainer = Color(0xFF6E2A12),
    onPrimaryContainer = Color(0xFFFFDBCF),
    secondary = Color(0xFFFFB68A),
    secondaryContainer = Color(0xFF5C3A12),
    onSecondaryContainer = Color(0xFFFFDDB8),
    tertiary = Color(0xFFE8C26A),
)

@Composable
fun TamilCalendarTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content,
    )
}
