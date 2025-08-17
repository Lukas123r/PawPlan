package de.lshorizon.pawplan.core.data.repo

import de.lshorizon.pawplan.core.domain.model.EventLog
import de.lshorizon.pawplan.core.domain.repo.EventLogRepository
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * In-memory storage of routine event history.
 */
class InMemoryEventLogRepository : EventLogRepository {

    private val items = MutableStateFlow<Map<Long, List<EventLog>>>(emptyMap())
    private val nextId = AtomicLong(1)

    override fun getEventsByRoutine(routineId: Long): Flow<List<EventLog>> =
        items.map { it[routineId] ?: emptyList() }

    override suspend fun addEvent(event: EventLog) {
        val newEvent = event.copy(id = nextId.getAndIncrement())
        items.update { map ->
            val list = map[event.routineId].orEmpty() + newEvent
            map + (event.routineId to list)
        }
    }
}
