package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.domain.analytics.Analytics
import de.lshorizon.pawplan.core.domain.analytics.AnalyticsEvent
import de.lshorizon.pawplan.core.domain.model.EventLog
import de.lshorizon.pawplan.core.domain.model.EventType
import de.lshorizon.pawplan.core.domain.repo.EventLogRepository
import de.lshorizon.pawplan.core.domain.repo.RoutineRepository
import java.time.LocalDateTime

/**
 * Delays a routine for a number of hours and logs the snooze.
 */
class SnoozeRoutine(
    private val repo: RoutineRepository,
    private val logRepo: EventLogRepository,
    private val analytics: Analytics,
) {
    suspend operator fun invoke(id: Long, hours: Long, now: LocalDateTime = LocalDateTime.now()) {
        require(hours > 0) { "Interval must be > 0" }
        val routine = repo.getRoutine(id) ?: return
        val base = routine.snoozedUntil ?: now
        val newTime = base.plusHours(hours)
        repo.updateRoutine(routine.copy(snoozedUntil = newTime))
        logRepo.addEvent(EventLog(routineId = id, timestamp = now, type = EventType.SNOOZE))
        analytics.track(AnalyticsEvent.REMINDER_ACTION_SNOOZE) // track snooze
    }
}
