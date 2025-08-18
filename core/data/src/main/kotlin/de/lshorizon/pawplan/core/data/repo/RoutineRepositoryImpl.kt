// core/data/src/main/kotlin/de/lshorizon/pawplan/core/data/repo/RoutineRepositoryImpl.kt
package de.lshorizon.pawplan.core.data.repo

import de.lshorizon.pawplan.core.domain.repo.RoutineRepository
import de.lshorizon.pawplan.core.domain.model.Routine
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

// Concrete repository that wires data sources with domain routines
@Singleton
class RoutineRepositoryImpl @Inject constructor(
  // TODO: inject data sources such as DAOs or API clients
) : RoutineRepository {

  override suspend fun listDueRoutines(until: Instant): List<Routine> {
    // TODO implement fetching of due routines; return empty list for now
    return emptyList()
  }

  override suspend fun markDone(id: String) {
    // TODO mark routine as completed in the data source
  }

  override fun getRoutines(): Flow<List<Routine>> {
    // TODO stream routines from the data source
    return flowOf(emptyList())
  }

  override suspend fun getRoutine(id: Long): Routine? {
    // TODO fetch a single routine by its id
    return null
  }

  override suspend fun addRoutine(routine: Routine): Routine {
    // TODO add a routine to the data source
    return routine
  }

  override suspend fun updateRoutine(routine: Routine) {
    // TODO update an existing routine in the data source
  }
}
