package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.domain.fakes.InMemoryEventLogRepository
import de.lshorizon.pawplan.core.domain.fakes.InMemoryRoutineRepository
import de.lshorizon.pawplan.core.domain.fakes.NoOpAnalytics
import de.lshorizon.pawplan.core.domain.model.Routine
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDateTime

/**
 * Ensures that marking a routine done updates its next due date.
 */
class MarkRoutineDoneTest {
    @Test
    fun `next due resets after completion`() = runTest {
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
        assertThat(updated.lastDone).isEqualTo(now)
        assertThat(updated.snoozedUntil).isNull()
        // next due is lastDone plus interval
        val nextDue = updated.lastDone!!.plusDays(updated.intervalDays.toLong())
        assertThat(nextDue).isEqualTo(now.plusDays(2))
    }
}
