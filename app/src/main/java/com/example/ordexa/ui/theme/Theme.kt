package com.example.ordexa.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = OrdexaGoldSecondary,
    onPrimary = OrdexaSlateDark,
    primaryContainer = OrdexaSlateMedium,
    onPrimaryContainer = OrdexaGoldContainer,
    secondary = OrdexaGoldSecondary,
    onSecondary = OrdexaSlateDark,
    background = OrdexaSlateDark,
    onBackground = Color.White,
    surface = OrdexaSlateMedium,
    onSurface = Color.White,
    surfaceVariant = OrdexaSlateLight,
    onSurfaceVariant = Color(0xFFCBD5E1),
    error = OrdexaStatusError,
    errorContainer = Color(0xFF7F1D1D)
)

private val LightColorScheme = lightColorScheme(
    primary = OrdexaBluePrimary,
    onPrimary = Color.White,
    primaryContainer = OrdexaBlueContainer,
    onPrimaryContainer = OnOrdexaBlueContainer,
    secondary = OrdexaGoldSecondary,
    onSecondary = Color(0xFF4A3400),
    secondaryContainer = OrdexaGoldContainer,
    onSecondaryContainer = OnOrdexaGoldContainer,
    background = OrdexaBackground,
    onBackground = OrdexaSlateDark,
    surface = OrdexaSurface,
    onSurface = OrdexaSlateDark,
    surfaceVariant = OrdexaSurfaceVariant,
    onSurfaceVariant = OnOrdexaSurfaceVariant,
    error = OrdexaStatusError,
    errorContainer = OrdexaStatusErrorBg
)

@Composable
fun OrdexaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
