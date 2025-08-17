package de.lshorizon.pawplan.core.domain.usecase

import de.lshorizon.pawplan.core.domain.model.Pet
import de.lshorizon.pawplan.core.domain.repo.PetRepository
import java.time.LocalDate

/**
 * Adds a new pet after validation.
 */
class CreatePet(private val repo: PetRepository) {

    suspend operator fun invoke(pet: Pet): Pet {
        require(pet.name.isNotBlank()) { "Name must not be empty" }
        pet.birthDate?.let {
            require(!it.isAfter(LocalDate.now())) { "Date must be today or earlier" }
        }
        return repo.addPet(pet)
    }
}
