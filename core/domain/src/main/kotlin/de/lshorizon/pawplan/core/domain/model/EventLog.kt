package de.lshorizon.pawplan.core.domain.model

import java.time.LocalDateTime

/**
 * Describes an action taken on a routine.
 */
enum class EventType { DONE, SNOOZE }

/**
 * Records when a routine was performed or delayed.
 */
data class EventLog(
    val id: Long = 0L,
    val routineId: Long,
    val timestamp: LocalDateTime,
    val type: EventType,
)
