package de.lshorizon.pawplan.core.domain.model

import java.time.LocalDate

/**
 * Represents a pet owned by the user.
 */
data class Pet(
    val id: Long = 0L,
    val name: String,
    val birthDate: LocalDate? = null,
)
