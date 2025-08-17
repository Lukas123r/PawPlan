package de.lshorizon.pawplan.domain.routines

import java.time.Instant

/**
 * Use case for listing routines that are due until the given time.
 */
fun interface ListDueRoutines {
    suspend operator fun invoke(until: Instant): List<RoutineDue>
}
