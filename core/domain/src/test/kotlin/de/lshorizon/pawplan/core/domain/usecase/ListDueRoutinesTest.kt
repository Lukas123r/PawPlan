package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.data.analytics.NoOpAnalytics
import de.lshorizon.pawplan.core.data.repo.InMemoryRoutineRepository
import de.lshorizon.pawplan.core.domain.model.Routine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

/**
 * Verifies that [ListDueRoutines] separates routines by their due window.
 */
class ListDueRoutinesTest {
    @Test
    fun `routines due today are returned`() = runBlocking {
        val repo = InMemoryRoutineRepository()
        val useCase = ListDueRoutines(repo, NoOpAnalytics())
        val now = LocalDateTime.now()
        repo.addRoutine(Routine(petId = 1, name = "A", intervalDays = 1, lastDone = now.minusDays(1)))
        val result = useCase(now).first()
        assertEquals(1, result.size)
        assertEquals("A", result.first().name)
    }

    @Test
    fun `routines due until tomorrow are returned`() = runBlocking {
        val repo = InMemoryRoutineRepository()
        val useCase = ListDueRoutines(repo, NoOpAnalytics())
        val now = LocalDateTime.now()
        repo.addRoutine(Routine(petId = 1, name = "A", intervalDays = 1, lastDone = now.minusDays(1)))
        repo.addRoutine(Routine(petId = 1, name = "B", intervalDays = 2, lastDone = now.minusDays(1)))
        val result = useCase(now.plusDays(1)).first()
        assertEquals(2, result.size)
    }

    @Test
    fun `routines due within three days are returned`() = runBlocking {
        val repo = InMemoryRoutineRepository()
        val useCase = ListDueRoutines(repo, NoOpAnalytics())
        val now = LocalDateTime.now()
        repo.addRoutine(Routine(petId = 1, name = "A", intervalDays = 1, lastDone = now.minusDays(1)))
        repo.addRoutine(Routine(petId = 1, name = "B", intervalDays = 2, lastDone = now.minusDays(1)))
        repo.addRoutine(Routine(petId = 1, name = "C", intervalDays = 4, lastDone = now.minusDays(1)))
        val result = useCase(now.plusDays(3)).first()
        assertEquals(3, result.size)
    }
}
