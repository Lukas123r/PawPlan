package de.lshorizon.pawplan.core.design

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Color definitions and tokens for PawPlan theme.
@Immutable
data class PawColors(
    val accent: Color,
    val pageBg: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val divider: Color,
    val textPrimary: Color,
    val textMuted: Color,
    val chipBorder: Color,
)

val LightPawColors = PawColors(
    accent = Color(0xFF0A84FF),
    pageBg = Color(0xFFFFFFFF),
    surface = Color(0xFFF2F2F7),
    surfaceElevated = Color(0xFFFFFFFF),
    divider = Color(0x1F000000),
    textPrimary = Color(0xFF000000),
    textMuted = Color(0x99000000),
    chipBorder = Color(0x33000000),
)

val DarkPawColors = PawColors(
    accent = Color(0xFF0A84FF),
    pageBg = Color(0xFF000000),
    surface = Color(0xFF1C1C1E),
    surfaceElevated = Color(0xFF2C2C2E),
    divider = Color(0x1FFFFFFF),
    textPrimary = Color(0xFFFFFFFF),
    textMuted = Color(0x99FFFFFF),
    chipBorder = Color(0x33FFFFFF),
)

val LocalPawColors = staticCompositionLocalOf { LightPawColors }
