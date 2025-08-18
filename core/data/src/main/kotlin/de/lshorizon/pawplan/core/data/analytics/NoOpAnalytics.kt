package de.lshorizon.pawplan.core.data.analytics

import de.lshorizon.pawplan.core.domain.analytics.Analytics
import de.lshorizon.pawplan.core.domain.analytics.AnalyticsEvent

/**
 * Simple analytics implementation that discards all events.
 */
class NoOpAnalytics : Analytics {
    override fun track(event: AnalyticsEvent) {
        // Intentionally left blank.
    }
}
