package de.lshorizon.pawplan.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import de.lshorizon.pawplan.core.data.repo.InMemoryEventLogRepository
import de.lshorizon.pawplan.core.data.repo.InMemoryPetRepository
import de.lshorizon.pawplan.core.data.repo.InMemoryRoutineRepository
import de.lshorizon.pawplan.core.domain.model.Pet
import de.lshorizon.pawplan.core.domain.model.Routine
import de.lshorizon.pawplan.ui.screen.routinedetail.RoutineDetailScreen
import de.lshorizon.pawplan.ui.screen.routinedetail.RoutineDetailViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Verifies that snoozing shifts the next due display.
 */
class RoutineDetailScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `snooze updates next due`() {
        val routineRepo = InMemoryRoutineRepository()
        val petRepo = InMemoryPetRepository()
        val eventRepo = InMemoryEventLogRepository()
        val now = LocalDateTime.now()
        val pet = runBlocking { petRepo.addPet(Pet(name = "Fido")) }
        val routine = runBlocking {
            routineRepo.addRoutine(Routine(petId = pet.id, name = "Walk", intervalDays = 1, lastDone = now.minusDays(1)))
        }
        val vm = RoutineDetailViewModel(routine.id, routineRepo, petRepo, eventRepo)
        composeRule.setContent { RoutineDetailScreen(routine.id, vm) }
        val formatter = DateTimeFormatter.ofPattern("MMM d, HH:mm")
        val expected = "Next due ${now.plusDays(1).format(formatter)}"
        composeRule.onNodeWithText("Snooze 24h").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText(expected).assertIsDisplayed()
    }
}
