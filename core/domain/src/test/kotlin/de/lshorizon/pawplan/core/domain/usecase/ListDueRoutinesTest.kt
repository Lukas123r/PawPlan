package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.domain.fakes.InMemoryRoutineRepository
import de.lshorizon.pawplan.core.domain.fakes.NoOpAnalytics
import de.lshorizon.pawplan.core.domain.model.Routine
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDateTime

/**
 * Verifies that [ListDueRoutines] separates routines by their due window.
 */
class ListDueRoutinesTest {
    @Test
    fun `routines due today are returned`() = runTest {
        val repo = InMemoryRoutineRepository()
        val useCase = ListDueRoutines(repo, NoOpAnalytics())
        val now = LocalDateTime.now()
        repo.addRoutine(Routine(petId = 1, name = "A", intervalDays = 1, lastDone = now.minusDays(1)))
        val result = useCase(now).first()
        assertThat(result).hasSize(1)
        assertThat(result.first().name).isEqualTo("A")
    }

    @Test
    fun `routines due until tomorrow are returned`() = runTest {
        val repo = InMemoryRoutineRepository()
        val useCase = ListDueRoutines(repo, NoOpAnalytics())
        val now = LocalDateTime.now()
        repo.addRoutine(Routine(petId = 1, name = "A", intervalDays = 1, lastDone = now.minusDays(1)))
        repo.addRoutine(Routine(petId = 1, name = "B", intervalDays = 2, lastDone = now.minusDays(1)))
        val result = useCase(now.plusDays(1)).first()
        assertThat(result).hasSize(2)
    }

    @Test
    fun `routines due within three days are returned`() = runTest {
        val repo = InMemoryRoutineRepository()
        val useCase = ListDueRoutines(repo, NoOpAnalytics())
        val now = LocalDateTime.now()
        repo.addRoutine(Routine(petId = 1, name = "A", intervalDays = 1, lastDone = now.minusDays(1)))
        repo.addRoutine(Routine(petId = 1, name = "B", intervalDays = 2, lastDone = now.minusDays(1)))
        repo.addRoutine(Routine(petId = 1, name = "C", intervalDays = 4, lastDone = now.minusDays(1)))
        val result = useCase(now.plusDays(3)).first()
        assertThat(result).hasSize(3)
    }
}
