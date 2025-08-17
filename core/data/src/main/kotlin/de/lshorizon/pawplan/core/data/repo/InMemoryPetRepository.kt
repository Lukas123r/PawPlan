package de.lshorizon.pawplan.core.data.repo

import de.lshorizon.pawplan.core.domain.model.Pet
import de.lshorizon.pawplan.core.domain.repo.PetRepository
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Simple in-memory storage for pets.
 */
class InMemoryPetRepository : PetRepository {

    private val items = MutableStateFlow<List<Pet>>(emptyList())
    private val nextId = AtomicLong(1)

    override fun getPets(): Flow<List<Pet>> = items

    override suspend fun getPet(id: Long): Pet? = items.value.find { it.id == id }

    override suspend fun addPet(pet: Pet): Pet {
        val newPet = pet.copy(id = nextId.getAndIncrement())
        items.update { it + newPet }
        return newPet
    }

    override suspend fun updatePet(pet: Pet) {
        items.update { list -> list.map { if (it.id == pet.id) pet else it } }
    }

    override suspend fun deletePet(id: Long) {
        items.update { list -> list.filterNot { it.id == id } }
    }
}
