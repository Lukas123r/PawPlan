// core/domain/src/main/kotlin/de/lshorizon/pawplan/domain/routines/ListDueRoutines.kt
package de.lshorizon.pawplan.domain.routines

import de.lshorizon.pawplan.core.domain.repo.RoutineRepository
import de.lshorizon.pawplan.core.domain.model.Routine
import java.time.Instant
import javax.inject.Inject

// Use case for retrieving routines due before a given time
class ListDueRoutines @Inject constructor(
  private val repository: RoutineRepository
) {
  suspend operator fun invoke(until: Instant): List<Routine> {
    return repository.listDueRoutines(until)
  }
}
