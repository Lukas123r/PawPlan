package de.lshorizon.pawplan.core.domain.fakes

import de.lshorizon.pawplan.core.domain.model.Routine
import de.lshorizon.pawplan.core.domain.repo.RoutineRepository
import java.time.Instant
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/** Simple in-memory [RoutineRepository] used in unit tests. */
class InMemoryRoutineRepository : RoutineRepository {
    private val items = MutableStateFlow<List<Routine>>(emptyList())
    private val nextId = AtomicLong(1)

    override fun getRoutines(): Flow<List<Routine>> = items

    override suspend fun getRoutine(id: Long): Routine? = items.value.find { it.id == id }

    override suspend fun addRoutine(routine: Routine): Routine {
        val newRoutine = routine.copy(id = nextId.getAndIncrement())
        items.update { it + newRoutine }
        return newRoutine
    }

    override suspend fun updateRoutine(routine: Routine) {
        items.update { list -> list.map { if (it.id == routine.id) routine else it } }
    }

    // Unused defaults for this project
    override suspend fun listDueRoutines(until: Instant): List<Routine> = emptyList()
    override suspend fun markDone(id: String) {}
}
