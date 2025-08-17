package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.domain.repo.PetRepository

/**
 * Removes a pet by its id.
 */
class DeletePet(private val repo: PetRepository) {
    suspend operator fun invoke(id: Long) {
        repo.deletePet(id)
    }
}
