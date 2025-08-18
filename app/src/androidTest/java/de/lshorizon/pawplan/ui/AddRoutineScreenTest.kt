package de.lshorizon.pawplan.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import de.lshorizon.pawplan.core.data.repo.InMemoryPetRepository
import de.lshorizon.pawplan.core.data.repo.InMemoryRoutineRepository
import de.lshorizon.pawplan.core.domain.model.Pet
import de.lshorizon.pawplan.ui.screen.addroutine.AddRoutineScreen
import de.lshorizon.pawplan.ui.screen.addroutine.AddRoutineViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

/**
 * Tests validation messages on the add routine form.
 */
class AddRoutineScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `empty title shows error`() {
        val routineRepo = InMemoryRoutineRepository()
        val petRepo = InMemoryPetRepository()
        val pet = runBlocking { petRepo.addPet(Pet(name = "Fido")) }
        val vm = AddRoutineViewModel(routineRepo, petRepo)
        vm.onPetChange(pet.id)
        composeRule.setContent { AddRoutineScreen(vm) }
        composeRule.onNodeWithText("Save").performClick()
        composeRule.onNodeWithText("Title is required").assertIsDisplayed()
    }
}
