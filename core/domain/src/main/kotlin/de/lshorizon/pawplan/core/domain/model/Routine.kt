package de.lshorizon.pawplan.core.domain.model

import java.time.LocalDateTime

/**
 * Describes a recurring care task for a pet.
 */
data class Routine(
    val id: Long = 0L,
    val petId: Long,
    val name: String,
    val intervalDays: Int,
    /** Optional category to group similar routines. */
    val category: String = "",
    /** Free-form note stored with the routine. */
    val notes: String = "",
    val lastDone: LocalDateTime? = null,
    val active: Boolean = true,
    val snoozedUntil: LocalDateTime? = null,
)
