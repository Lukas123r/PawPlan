package de.lshorizon.pawplan.core.domain.fakes

import de.lshorizon.pawplan.core.domain.model.EventLog
import de.lshorizon.pawplan.core.domain.repo.EventLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/** Minimal event log storage for tests. */
class InMemoryEventLogRepository : EventLogRepository {
    private val events = MutableStateFlow<List<EventLog>>(emptyList())

    override fun getEventsByRoutine(routineId: Long): Flow<List<EventLog>> =
        events

    override suspend fun addEvent(event: EventLog) {
        events.update { it + event }
    }
}
