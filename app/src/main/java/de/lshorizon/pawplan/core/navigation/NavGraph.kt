package de.lshorizon.pawplan.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.lshorizon.pawplan.ui.screen.AddPetScreen
import de.lshorizon.pawplan.ui.screen.AddRoutineScreen
import de.lshorizon.pawplan.ui.screen.home.HomeScreen
import de.lshorizon.pawplan.ui.screen.OnboardingScreen
import de.lshorizon.pawplan.ui.screen.PetsScreen
import de.lshorizon.pawplan.ui.screen.RoutineScreen
import de.lshorizon.pawplan.ui.screen.SettingsScreen

/**
 * Central navigation graph connecting all screens.
 */
@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) { HomeScreen() }
        composable(NavRoutes.Pets.route) { PetsScreen() }
        composable(NavRoutes.AddPet.route) { AddPetScreen() }
        composable(NavRoutes.AddRoutine.route) { AddRoutineScreen() }
        composable(NavRoutes.Routine.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            RoutineScreen(id)
        }
        composable(NavRoutes.Settings.route) { SettingsScreen() }
        composable(NavRoutes.Onboarding.route) { OnboardingScreen() }
    }
}
