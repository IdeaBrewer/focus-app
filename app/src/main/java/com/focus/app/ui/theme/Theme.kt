package com.focus.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFE60012),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE8E8),
    onPrimaryContainer = Color(0xFF410001),
    secondary = Color(0xFF77574A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDDD1),
    onSecondaryContainer = Color(0xFF2C160C),
    tertiary = Color(0xFF665E32),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFEFE2AC),
    onTertiaryContainer = Color(0xFF201C00),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color.White,
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF1F1B16),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF1F1B16),
    surfaceVariant = Color(0xFFF5DED5),
    onSurfaceVariant = Color(0xFF53443C),
    outline = Color(0xFF85736B),
    outlineVariant = Color(0xFFD7C2B8),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF34302A),
    inverseOnSurface = Color(0xFFFBEEE7),
    inversePrimary = Color(0xFFFFB4AB)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB4AB),
    onPrimary = Color(0xFF690005),
    primaryContainer = Color(0xFF93000A),
    onPrimaryContainer = Color(0xFFFFDAD6),
    secondary = Color(0xFFE7BEAE),
    onSecondary = Color(0xFF442B20),
    secondaryContainer = Color(0xFF5D4035),
    onSecondaryContainer = Color(0xFFFFDAD6),
    tertiary = Color(0xFFD1C691),
    onTertiary = Color(0xFF363005),
    tertiaryContainer = Color(0xFF4E4719),
    onTertiaryContainer = Color(0xFFEFE2AC),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF17120E),
    onBackground = Color(0xFFEBE0DB),
    surface = Color(0xFF17120E),
    onSurface = Color(0xFFEBE0DB),
    surfaceVariant = Color(0xFF53443C),
    onSurfaceVariant = Color(0xFFD7C2B8),
    outline = Color(0xFFA08D84),
    outlineVariant = Color(0xFF53443C),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFEBE0DB),
    inverseOnSurface = Color(0xFF34302A),
    inversePrimary = Color(0xFF93000A)
)

@Composable
fun FocusTheme(
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
        shapes = Shapes,
        content = content
    )
}