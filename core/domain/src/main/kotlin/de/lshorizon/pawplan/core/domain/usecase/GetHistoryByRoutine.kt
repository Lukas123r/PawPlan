package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.domain.model.EventLog
import de.lshorizon.pawplan.core.domain.repo.EventLogRepository
import kotlinx.coroutines.flow.Flow

/**
 * Streams the event history for a routine.
 */
class GetHistoryByRoutine(private val repo: EventLogRepository) {
    operator fun invoke(routineId: Long): Flow<List<EventLog>> =
        repo.getEventsByRoutine(routineId)
}
