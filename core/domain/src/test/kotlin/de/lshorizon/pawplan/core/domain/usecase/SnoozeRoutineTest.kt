package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.data.analytics.NoOpAnalytics
import de.lshorizon.pawplan.core.data.repo.InMemoryEventLogRepository
import de.lshorizon.pawplan.core.data.repo.InMemoryRoutineRepository
import de.lshorizon.pawplan.core.domain.model.Routine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

/**
 * Checks that snoozing shifts the routine by the requested hours.
 */
class SnoozeRoutineTest {
    @Test
    fun `snooze adjusts next due time`() = runBlocking {
        val routineRepo = InMemoryRoutineRepository()
        val eventRepo = InMemoryEventLogRepository()
        val useCase = SnoozeRoutine(routineRepo, eventRepo, NoOpAnalytics())
        val now = LocalDateTime.now()
        val routine = routineRepo.addRoutine(Routine(petId = 1, name = "Feed", intervalDays = 1, lastDone = now.minusDays(1)))
        listOf(6L, 24L, 48L).forEach { hrs ->
            useCase(routine.id, hrs, now)
            val updated = routineRepo.getRoutine(routine.id)!!
            assertEquals(now.plusHours(hrs), updated.snoozedUntil)
        }
    }
}
