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
import de.lshorizon.pawplan.data.onboarding.OnboardingRepository
import de.lshorizon.pawplan.core.navigation.NavRoutes
import kotlinx.coroutines.flow.first
import de.lshorizon.pawplan.core.design.PawPlanTheme
import de.lshorizon.pawplan.core.navigation.NavGraph

/**
 * Main activity launching the PawPlan navigation graph.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PawPlanTheme {
                val repo = remember { OnboardingRepository(applicationContext) }
                var start by remember { mutableStateOf<String?>(null) }
                LaunchedEffect(Unit) {
                    val seen = repo.isOnboardingComplete.first()
                    start = if (seen) NavRoutes.Home.route else NavRoutes.Onboarding.route
                }
                start?.let { NavGraph(startDestination = it) }
            }
        }
    }
}
