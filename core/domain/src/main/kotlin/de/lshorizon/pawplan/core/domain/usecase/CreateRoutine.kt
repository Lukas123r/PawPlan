package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.domain.analytics.Analytics
import de.lshorizon.pawplan.core.domain.analytics.AnalyticsEvent
import de.lshorizon.pawplan.core.domain.model.Routine
import de.lshorizon.pawplan.core.domain.repo.RoutineRepository
import java.time.LocalDateTime

/**
 * Creates a new care routine.
 */
class CreateRoutine(
    private val repo: RoutineRepository,
    private val analytics: Analytics,
) {

    suspend operator fun invoke(routine: Routine): Routine {
        validate(routine)
        routine.lastDone?.let {
            require(!it.toLocalDate().isAfter(LocalDateTime.now().toLocalDate())) { "Date must be today or earlier" }
        }
        val saved = repo.addRoutine(routine)
        analytics.track(AnalyticsEvent.ADD_ROUTINE) // track routine creation
        return saved
    }

    private fun validate(routine: Routine) {
        require(routine.name.isNotBlank()) { "Name must not be empty" }
        require(routine.intervalDays > 0) { "Interval must be > 0" }
    }
}
