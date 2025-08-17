package de.lshorizon.pawplan.core.navigation

/**
 * Collection of navigation routes used throughout the app.
 */
sealed class NavRoutes(val route: String) {
    /** Route for the main home screen. */
    data object Home : NavRoutes("home")

    /** Route displaying the list of pets. */
    data object Pets : NavRoutes("pets")

    /** Route to add a new pet. */
    data object AddPet : NavRoutes("addPet")

    /** Route to add a routine. */
    data object AddRoutine : NavRoutes("addRoutine")

    /** Route to show a specific routine by id. */
    data object Routine : NavRoutes("routine/{id}") {
        fun create(id: String) = "routine/$id"
    }

    /** Route for general settings. */
    data object Settings : NavRoutes("settings")

    /** Route for the onboarding flow. */
    data object Onboarding : NavRoutes("onboarding")
}
