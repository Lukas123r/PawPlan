package de.lshorizon.pawplan.core.domain.repo

import de.lshorizon.pawplan.core.domain.model.Routine
import kotlinx.coroutines.flow.Flow

/**
 * Handles persistence of routines.
 */
interface RoutineRepository {
    fun getRoutines(): Flow<List<Routine>>
    suspend fun getRoutine(id: Long): Routine?
    suspend fun addRoutine(routine: Routine): Routine
    suspend fun updateRoutine(routine: Routine)
}
