package de.lshorizon.pawplan.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import de.lshorizon.pawplan.core.data.repo.InMemoryPetRepository
import de.lshorizon.pawplan.core.data.repo.InMemoryRoutineRepository
import de.lshorizon.pawplan.core.domain.model.Pet
import de.lshorizon.pawplan.core.domain.model.Routine
import de.lshorizon.pawplan.ui.screen.home.HomeScreen
import de.lshorizon.pawplan.ui.screen.home.HomeViewModel
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import kotlinx.coroutines.runBlocking

/**
 * UI test ensuring home screen sections render.
 */
class HomeScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `home shows due sections`() {
        val routineRepo = InMemoryRoutineRepository()
        val petRepo = InMemoryPetRepository()
        // add sample pet and routines
        runBlocking {
            val pet = petRepo.addPet(Pet(name = "Fido"))
            routineRepo.addRoutine(Routine(petId = pet.id, name = "Walk", intervalDays = 1, lastDone = LocalDateTime.now().minusDays(1)))
            routineRepo.addRoutine(Routine(petId = pet.id, name = "Feed", intervalDays = 2, lastDone = LocalDateTime.now()))
        }
        val vm = HomeViewModel(routineRepo, petRepo)
        composeRule.setContent { HomeScreen(vm) }
        composeRule.onNodeWithText("Due Now").assertIsDisplayed()
        composeRule.onNodeWithText("Coming Up").assertIsDisplayed()
    }
}
