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
    val lastDone: LocalDateTime? = null,
    val active: Boolean = true,
    val snoozedUntil: LocalDateTime? = null,
)
