package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.domain.model.Pet
import de.lshorizon.pawplan.core.domain.repo.PetRepository
import kotlinx.coroutines.flow.Flow

/**
 * Emits all pets as a stream.
 */
class ListPets(private val repo: PetRepository) {
    operator fun invoke(): Flow<List<Pet>> = repo.getPets()
}
