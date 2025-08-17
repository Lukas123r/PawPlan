package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.domain.model.Routine
import de.lshorizon.pawplan.core.domain.repo.RoutineRepository
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Emits routines that are due until the given time.
 */
class ListDueRoutines(private val repo: RoutineRepository) {
    operator fun invoke(until: LocalDateTime): Flow<List<Routine>> =
        repo.getRoutines().map { list ->
            list.filter { it.active && isDue(it, until) }
        }

    private fun isDue(routine: Routine, until: LocalDateTime): Boolean {
        val last = routine.lastDone ?: LocalDateTime.MIN
        val dueTime = last.plusDays(routine.intervalDays.toLong())
        val snoozed = routine.snoozedUntil ?: dueTime
        val actualDue = if (snoozed.isAfter(dueTime)) snoozed else dueTime
        return actualDue <= until
        }
}
