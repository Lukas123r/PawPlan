package de.lshorizon.pawplan.ui

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.lshorizon.pawplan.core.design.PawPlanTheme
import de.lshorizon.pawplan.core.data.prefs.PrefsRepository
import de.lshorizon.pawplan.ui.screen.onboarding.OnboardingScreen
import de.lshorizon.pawplan.ui.screen.onboarding.OnboardingViewModel

/** Screenshot test rendering the onboarding screen with Paparazzi. */
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

@Composable
private fun AppPreviewContent() {
    // Provide a fake repository so the onboarding screen can render without real data.
    PawPlanTheme {
        val nav = rememberNavController()
        val vm = OnboardingViewModel(object : PrefsRepository {
            override suspend fun hasSeenOnboarding() = false
            override suspend fun setOnboardingDone(value: Boolean) {}
        })
        OnboardingScreen(navController = nav, vm = vm)
    }
}
