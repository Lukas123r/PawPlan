package de.lshorizon.pawplan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import de.lshorizon.pawplan.core.data.onboarding.OnboardingRepository
import de.lshorizon.pawplan.core.navigation.NavRoutes
import de.lshorizon.pawplan.core.design.PawPlanTheme
import de.lshorizon.pawplan.core.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main activity launching the PawPlan navigation graph.
 */
// Entry point for Hilt dependency injection
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var onboardingRepository: OnboardingRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PawPlanTheme {
                var start by remember { mutableStateOf<String?>(null) }
                LaunchedEffect(Unit) {
                    val seen = onboardingRepository.isCompleted()
                    start = if (seen) NavRoutes.Home.route else NavRoutes.Onboarding.route
                }
                start?.let { NavGraph(startDestination = it) }
            }
        }
    }
}
