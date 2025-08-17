package de.lshorizon.pawplan.core.domain.repo

import de.lshorizon.pawplan.core.domain.model.EventLog
import kotlinx.coroutines.flow.Flow

/**
 * Stores routine execution events.
 */
interface EventLogRepository {
    fun getEventsByRoutine(routineId: Long): Flow<List<EventLog>>
    suspend fun addEvent(event: EventLog)
}
