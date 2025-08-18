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
 * Ensures that marking a routine done updates its next due date.
 */
class MarkRoutineDoneTest {
    @Test
    fun `next due resets after completion`() = runBlocking {
        val routineRepo = InMemoryRoutineRepository()
        val eventRepo = InMemoryEventLogRepository()
        val useCase = MarkRoutineDone(routineRepo, eventRepo, NoOpAnalytics())
        val now = LocalDateTime.now()
        val routine = routineRepo.addRoutine(
            Routine(petId = 1, name = "Walk", intervalDays = 2, lastDone = now.minusDays(2))
        )
        useCase(routine.id, now)
        val updated = routineRepo.getRoutine(routine.id)!!
        // lastDone should be updated and snooze cleared
        assertEquals(now, updated.lastDone)
        assertEquals(null, updated.snoozedUntil)
        // next due is lastDone plus interval
        val nextDue = updated.lastDone!!.plusDays(updated.intervalDays.toLong())
        assertEquals(now.plusDays(2), nextDue)
    }
}
