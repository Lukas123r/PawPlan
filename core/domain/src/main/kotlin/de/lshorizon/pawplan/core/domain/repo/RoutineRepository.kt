// core/domain/src/main/kotlin/de/lshorizon/pawplan/core/domain/repo/RoutineRepository.kt
// Interface for accessing and updating routine data
package de.lshorizon.pawplan.core.domain.repo

import de.lshorizon.pawplan.core.domain.model.Routine
import java.time.Instant
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    suspend fun listDueRoutines(until: Instant): List<Routine> = emptyList()
    suspend fun markDone(id: String) {}
    fun getRoutines(): Flow<List<Routine>>
    suspend fun getRoutine(id: Long): Routine?
    suspend fun addRoutine(routine: Routine): Routine
    suspend fun updateRoutine(routine: Routine)
}
