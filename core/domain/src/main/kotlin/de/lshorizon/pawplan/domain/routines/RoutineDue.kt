package de.lshorizon.pawplan.domain.routines

import java.time.Instant

/**
 * Simple model describing a routine and when it becomes due.
 */
data class RoutineDue(
    val id: String,
    val dueAt: Instant
)
