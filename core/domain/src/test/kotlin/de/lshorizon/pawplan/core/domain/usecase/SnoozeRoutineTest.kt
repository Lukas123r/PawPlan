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
 * Checks that snoozing shifts the routine by the requested hours.
 */
class SnoozeRoutineTest {
    @Test
    fun `snooze adjusts next due time`() = runTest {
        val routineRepo = InMemoryRoutineRepository()
        val eventRepo = InMemoryEventLogRepository()
        val useCase = SnoozeRoutine(routineRepo, eventRepo, NoOpAnalytics())
        val now = LocalDateTime.now()
        val routine = routineRepo.addRoutine(
            Routine(petId = 1, name = "Feed", intervalDays = 1, lastDone = now.minusDays(1))
        )
        useCase(routine.id, 24L, now)
        val updated = routineRepo.getRoutine(routine.id)!!
        assertThat(updated.snoozedUntil).isEqualTo(now.plusHours(24))
    }
}
