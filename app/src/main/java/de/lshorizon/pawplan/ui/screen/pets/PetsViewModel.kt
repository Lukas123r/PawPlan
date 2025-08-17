package de.lshorizon.pawplan.ui.screen.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lshorizon.pawplan.core.data.repo.InMemoryPetRepository
import de.lshorizon.pawplan.core.domain.model.Pet
import de.lshorizon.pawplan.core.domain.usecase.ListPets
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel that streams the current list of pets via [ListPets].
 */
class PetsViewModel(
    private val repo: InMemoryPetRepository = InMemoryPetRepository()
) : ViewModel() {

    private val listPets = ListPets(repo)

    /** State flow emitting all known pets. */
    val pets: StateFlow<List<Pet>> =
        listPets().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
