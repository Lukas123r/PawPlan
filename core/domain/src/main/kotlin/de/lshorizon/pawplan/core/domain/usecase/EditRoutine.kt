package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.domain.model.Routine
import de.lshorizon.pawplan.core.domain.repo.RoutineRepository
import java.time.LocalDateTime

/**
 * Updates a routine's data.
 */
class EditRoutine(private val repo: RoutineRepository) {

    suspend operator fun invoke(routine: Routine) {
        validate(routine)
        routine.lastDone?.let {
            require(!it.toLocalDate().isAfter(LocalDateTime.now().toLocalDate())) { "Date must be today or earlier" }
        }
        repo.updateRoutine(routine)
    }

    private fun validate(routine: Routine) {
        require(routine.name.isNotBlank()) { "Name must not be empty" }
        require(routine.intervalDays > 0) { "Interval must be > 0" }
    }
}
