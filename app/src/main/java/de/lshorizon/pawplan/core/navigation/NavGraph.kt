package de.lshorizon.pawplan.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.lshorizon.pawplan.ui.screen.addroutine.AddRoutineScreen
import de.lshorizon.pawplan.ui.screen.home.HomeScreen
import de.lshorizon.pawplan.ui.screen.onboarding.OnboardingScreen
import de.lshorizon.pawplan.ui.screen.pets.PetsScreen
import de.lshorizon.pawplan.ui.screen.RoutineScreen
import de.lshorizon.pawplan.ui.screen.settings.SettingsScreen
import de.lshorizon.pawplan.ui.screen.addpet.AddPetScreen
import de.lshorizon.pawplan.ui.screen.addpet.EditPetScreen

/**
 * Central navigation graph connecting all screens.
 */
@Composable
fun NavGraph(navController: NavHostController = rememberNavController(), startDestination: String = NavRoutes.Home.route) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoutes.Home.route) { HomeScreen() }
        composable(NavRoutes.Pets.route) {
            PetsScreen(
                onAddPet = { navController.navigate(NavRoutes.AddPet.route) },
                onEditPet = { id -> navController.navigate(NavRoutes.EditPet.create(id)) }
            )
        }
        composable(NavRoutes.AddPet.route) {
            AddPetScreen(onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.EditPet.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0L
            EditPetScreen(petId = id, onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.AddRoutine.route) { AddRoutineScreen() }
        composable(NavRoutes.Routine.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            RoutineScreen(id)
        }
        composable(NavRoutes.Settings.route) { SettingsScreen() }
        composable(NavRoutes.Onboarding.route) {
            OnboardingScreen(onFinished = {
                navController.navigate(NavRoutes.AddPet.route) {
                    popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                }
            })
        }
    }
}
