package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.domain.model.EventLog
import de.lshorizon.pawplan.core.domain.repo.EventLogRepository
import de.lshorizon.pawplan.core.domain.repo.RoutineRepository
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Marks a routine as completed at the given time.
 */
class MarkRoutineDone(
    private val routineRepo: RoutineRepository,
    private val logRepo: EventLogRepository,
) {
    suspend operator fun invoke(id: Long, now: LocalDateTime) {
        require(!now.toLocalDate().isAfter(LocalDate.now())) { "Date must be today or earlier" }
        val routine = routineRepo.getRoutine(id) ?: return
        routineRepo.updateRoutine(routine.copy(lastDone = now, snoozedUntil = null))
        logRepo.addEvent(EventLog(routineId = id, timestamp = now))
    }
}
