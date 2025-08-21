package de.lshorizon.pawplan.ui

import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.DeviceConfig
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.lshorizon.pawplan.core.design.PawPlanTheme
import de.lshorizon.pawplan.ui.screen.onboarding.OnboardingScreen
import de.lshorizon.pawplan.ui.screen.onboarding.OnboardingViewModel
import de.lshorizon.pawplan.core.data.prefs.PrefsRepository

/** Paparazzi snapshot tests capturing key UI screens. */
@RunWith(AndroidJUnit4::class)
class ScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun onboarding_light() {
        paparazzi.snapshot {
            AppPreviewContent()
        }
    }
}

/** Renders the onboarding screen within the app theme for snapshots. */
@Composable
private fun AppPreviewContent() {
    PawPlanTheme {
        OnboardingScreen(
            navController = rememberNavController(),
            vm = OnboardingViewModel(FakePrefsRepository())
        )
    }
}

/** Minimal no-op prefs store for previewing. */
private class FakePrefsRepository : PrefsRepository {
    override suspend fun hasSeenOnboarding() = false
    override suspend fun setOnboardingDone(value: Boolean) {}
}
