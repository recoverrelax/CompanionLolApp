package com.companion.lol.app.compose.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme =
  darkColorScheme(
    primary = Gold1,
    onPrimary = Black,
    secondary = Blue3,
    onSecondary = White,
    tertiary = Blue6,
    onTertiary = White,
    background = Black,
    onBackground = White,
    surface = DarkPlatinium,
    onSurface = White,
    surfaceVariant = GreyCool,
    onSurfaceVariant = Gold2,
    outline = Gold4,
  )

// TODO
// private val DarkColorScheme

@Composable
fun CompanionAppTheme(content: @Composable () -> Unit) {
  MaterialTheme(colorScheme = LightColorScheme, typography = Typography, content = content)
}
