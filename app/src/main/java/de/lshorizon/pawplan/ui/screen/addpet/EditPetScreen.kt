package de.lshorizon.pawplan.ui.screen.addpet

import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import de.lshorizon.pawplan.core.data.repo.InMemoryPetRepository
import de.lshorizon.pawplan.core.domain.model.Pet
import de.lshorizon.pawplan.core.domain.usecase.UpdatePet
import kotlinx.coroutines.launch

/**
 * Screen to modify an existing pet identified by [petId].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPetScreen(
    petId: Long,
    onBack: () -> Unit = {},
    viewModel: EditPetViewModel = viewModel(factory = EditPetViewModel.Factory(petId)),
) {
    val state = viewModel.state

    Scaffold(topBar = { TopAppBar(title = { Text("Edit Pet") }) }) { padding ->
        PetForm(
            state = state,
            onNameChange = viewModel::onNameChange,
            onSpeciesChange = viewModel::onSpeciesChange,
            onBreedChange = viewModel::onBreedChange,
            onBirthdayChange = viewModel::onBirthdayChange,
            onWeightChange = viewModel::onWeightChange,
            onPhotoFromGallery = viewModel::onPhotoUriChange,
            onPhotoFromCamera = viewModel::onPhotoBitmapChange,
            onNotesChange = viewModel::onNotesChange,
            onSave = { viewModel.save(onBack) },
            // Reuse PetForm with surrounding padding
            modifier = Modifier.padding(padding)
        )
    }
}

/**
 * ViewModel responsible for editing an existing pet.
 */
class EditPetViewModel(
    private val petId: Long,
    private val repo: InMemoryPetRepository = InMemoryPetRepository(),
) : PetFormViewModel(repo) {

    private val updatePet = UpdatePet(repo)

    init {
        // Load current pet data to prefill the form
        viewModelScope.launch {
            repo.getPet(petId)?.let { pet ->
                state = state.copy(name = pet.name, birthday = pet.birthDate)
            }
        }
    }

    /** Save the modified pet after validation. */
    fun save(onSaved: () -> Unit) {
        if (state.name.isBlank()) {
            state = state.copy(nameError = true)
            return
        }
        viewModelScope.launch {
            updatePet(Pet(id = petId, name = state.name, birthDate = state.birthday))
            onSaved()
        }
    }

    companion object {
        /** Factory to create an [EditPetViewModel] with a pet id. */
        fun Factory(id: Long) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return EditPetViewModel(id) as T
            }
        }
    }
}
