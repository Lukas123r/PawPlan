// core/domain/src/main/kotlin/de/lshorizon/pawplan/domain/routines/ListDueRoutines.kt
package de.lshorizon.pawplan.domain.routines

import de.lshorizon.pawplan.core.domain.repo.RoutineRepository
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import kotlinx.coroutines.flow.first

/**
 * Use case that lists routines due before a given time.
 */
class ListDueRoutines @Inject constructor(
  private val repository: RoutineRepository
) {
  /**
   * Returns all routines that are due until [until].
   */
  suspend operator fun invoke(until: Instant): List<RoutineDue> {
    val zone = ZoneId.systemDefault()
    return repository.getRoutines().first().mapNotNull { routine ->
      val last = routine.lastDone ?: LocalDateTime.MIN
      val dueTime = last.plusDays(routine.intervalDays.toLong())
      val snoozed = routine.snoozedUntil ?: dueTime
      val actualDue = if (snoozed.isAfter(dueTime)) snoozed else dueTime
      val dueInstant = actualDue.atZone(zone).toInstant()
      if (dueInstant <= until) RoutineDue(routine.id.toString(), dueInstant) else null
    }
  }
}

