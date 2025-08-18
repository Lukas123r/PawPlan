package de.lshorizon.pawplan.core.domain.analytics

/**
 * Interface for anonymous event tracking.
 */
interface Analytics {
    /** Record the given event. */
    fun track(event: AnalyticsEvent)
}

/**
 * Enumerates all analytics events used in the app.
 */
enum class AnalyticsEvent {
    FIRST_OPEN,
    ADD_PET,
    ADD_ROUTINE,
    REMINDER_FIRED,
    REMINDER_ACTION_DONE,
    REMINDER_ACTION_SNOOZE,
    EXPORT_OK,
    IMPORT_OK,
    IMPORT_FAIL,
}
