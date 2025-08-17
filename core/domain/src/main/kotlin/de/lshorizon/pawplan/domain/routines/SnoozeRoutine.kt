package de.lshorizon.pawplan.domain.routines

import kotlin.time.Duration

/**
 * Use case for snoozing a routine for the given duration.
 */
fun interface SnoozeRoutine {
    suspend operator fun invoke(id: String, duration: Duration)
}
