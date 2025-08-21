package de.lshorizon.pawplan.core.domain.fakes

import de.lshorizon.pawplan.core.domain.analytics.Analytics
import de.lshorizon.pawplan.core.domain.analytics.AnalyticsEvent

/** No-op analytics implementation for testing. */
class NoOpAnalytics : Analytics {
    override fun track(event: AnalyticsEvent) { /* no-op */ }
}
