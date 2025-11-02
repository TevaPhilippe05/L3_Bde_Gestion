package com.example.bdeorga.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val DarkColorScheme = darkColorScheme(
    primary = HotPink,
    onPrimary = PureWhiteText,
    background = DarkPageBackground,     // Fond de page
    onBackground = PureWhiteText,
    surface = DarkCardSurface,           // Fond des cartes
    onSurface = PureWhiteText,
    surfaceContainer = DarkBarColor,     // Fond des barres haut/bas
    onSurfaceVariant = MutedDarkText
)

private val LightColorScheme = lightColorScheme(
    primary = HotPink,
    onPrimary = PureWhiteText,
    background = LightPageBackground,    // Fond de page
    onBackground = DarkPurpleText,
    surface = LightCardSurface,          // Fond des cartes
    onSurface = DarkPurpleText,
    surfaceContainer = LightBarColor,    // Fond des barres
    onSurfaceVariant = MutedPurpleGray
)

@Composable
fun MyBdeOrgaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}