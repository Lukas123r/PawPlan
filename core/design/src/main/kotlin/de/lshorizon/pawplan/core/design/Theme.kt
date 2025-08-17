package de.lshorizon.pawplan.core.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// Hosts the app's Material 3 theme and system UI integration.
@Composable
fun PawPlanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val pawColors = if (darkTheme) DarkPawColors else LightPawColors
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = pawColors.accent,
            background = pawColors.pageBg,
            surface = pawColors.surface,
            onPrimary = Color.White,
            onBackground = pawColors.textPrimary,
            onSurface = pawColors.textPrimary,
        )
    } else {
        lightColorScheme(
            primary = pawColors.accent,
            background = pawColors.pageBg,
            surface = pawColors.surface,
            onPrimary = Color.White,
            onBackground = pawColors.textPrimary,
            onSurface = pawColors.textPrimary,
        )
    }

    val systemUi = rememberSystemUiController()
    val useDarkIcons = !darkTheme
    SideEffect {
        systemUi.setSystemBarsColor(
            color = pawColors.pageBg,
            darkIcons = useDarkIcons
        )
    }

    CompositionLocalProvider(LocalPawColors provides pawColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = PawTypography,
            shapes = PawShapes,
            content = content
        )
    }
}

object PawPlanSpacing {
    // Base spacing unit referenced across layouts.
    val base = 16.dp
}

@Preview(name = "Light")
@Composable
private fun PawPlanThemePreviewLight() {
    PawPlanTheme(darkTheme = false) {
        Surface(
            modifier = Modifier
                .size(100.dp)
                .padding(PawPlanSpacing.base),
            color = LocalPawColors.current.surface
        ) {
            Text("Light", color = LocalPawColors.current.textPrimary)
        }
    }
}

@Preview(name = "Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PawPlanThemePreviewDark() {
    PawPlanTheme(darkTheme = true) {
        Surface(
            modifier = Modifier
                .size(100.dp)
                .padding(PawPlanSpacing.base),
            color = LocalPawColors.current.surface
        ) {
            Text("Dark", color = LocalPawColors.current.textPrimary)
        }
    }
}
