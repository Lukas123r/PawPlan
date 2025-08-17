package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.domain.repo.RoutineRepository
import java.time.LocalDateTime

/**
 * Delays a routine for a number of hours.
 */
class SnoozeRoutine(private val repo: RoutineRepository) {
    suspend operator fun invoke(id: Long, hours: Long) {
        require(hours > 0) { "Interval must be > 0" }
        val routine = repo.getRoutine(id) ?: return
        val base = routine.snoozedUntil ?: LocalDateTime.now()
        val newTime = base.plusHours(hours)
        repo.updateRoutine(routine.copy(snoozedUntil = newTime))
    }
}
