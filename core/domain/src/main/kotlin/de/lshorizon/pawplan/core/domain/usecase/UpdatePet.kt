package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.domain.model.Pet
import de.lshorizon.pawplan.core.domain.repo.PetRepository
import java.time.LocalDate

/**
 * Updates an existing pet while checking its data.
 */
class UpdatePet(private val repo: PetRepository) {

    suspend operator fun invoke(pet: Pet) {
        require(pet.name.isNotBlank()) { "Name must not be empty" }
        pet.birthDate?.let {
            require(!it.isAfter(LocalDate.now())) { "Date must be today or earlier" }
        }
        repo.updatePet(pet)
    }
}
