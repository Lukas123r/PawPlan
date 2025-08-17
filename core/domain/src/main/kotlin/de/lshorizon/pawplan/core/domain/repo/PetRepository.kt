package de.lshorizon.pawplan.core.domain.repo

import de.lshorizon.pawplan.core.domain.model.Pet
import kotlinx.coroutines.flow.Flow

/**
 * Provides access to stored pets.
 */
interface PetRepository {
    fun getPets(): Flow<List<Pet>>
    suspend fun getPet(id: Long): Pet?
    suspend fun addPet(pet: Pet): Pet
    suspend fun updatePet(pet: Pet)
    suspend fun deletePet(id: Long)
}
