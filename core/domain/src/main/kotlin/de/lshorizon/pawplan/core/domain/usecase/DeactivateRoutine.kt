package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.domain.repo.RoutineRepository

/**
 * Disables a routine temporarily.
 */
class DeactivateRoutine(private val repo: RoutineRepository) {
    suspend operator fun invoke(id: Long) {
        val routine = repo.getRoutine(id) ?: return
        repo.updateRoutine(routine.copy(active = false))
    }
}
