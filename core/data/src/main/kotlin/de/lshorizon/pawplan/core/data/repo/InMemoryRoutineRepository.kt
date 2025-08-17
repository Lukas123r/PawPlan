package de.lshorizon.pawplan.core.data.repo

import de.lshorizon.pawplan.core.domain.model.Routine
import de.lshorizon.pawplan.core.domain.repo.RoutineRepository
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * In-memory routine repository used for testing and prototyping.
 */
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
}
