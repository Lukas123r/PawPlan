package de.lshorizon.pawplan.core.domain.model

import java.time.LocalDateTime

/**
 * Records when a routine was performed.
 */
data class EventLog(
    val id: Long = 0L,
    val routineId: Long,
    val timestamp: LocalDateTime,
)
