package de.lshorizon.pawplan.ui.screen.addpet

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import de.lshorizon.pawplan.core.data.repo.InMemoryPetRepository
import de.lshorizon.pawplan.core.domain.model.Pet
import de.lshorizon.pawplan.core.domain.usecase.CreatePet
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

/**
 * Screen to create a brand new pet entry.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    viewModel: AddPetViewModel = viewModel(),
    onBack: () -> Unit = {},
) {
    // Collect current form state from the ViewModel
    val state = viewModel.state

    Scaffold(topBar = { TopAppBar(title = { Text("Add Pet") }) }) { padding ->
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
            modifier = Modifier.padding(padding)
        )
    }
}

/**
 * ViewModel storing form data for adding a pet and performing validation.
 */
class AddPetViewModel(
    private val repo: InMemoryPetRepository = InMemoryPetRepository(),
) : PetFormViewModel(repo) {

    private val createPet = CreatePet(repo)

    /** Trigger creation if the form is valid. */
    fun save(onSaved: () -> Unit) {
        if (state.name.isBlank()) {
            state = state.copy(nameError = true)
            return
        }
        viewModelScope.launch {
            createPet(Pet(name = state.name, birthDate = state.birthday))
            onSaved()
        }
    }
}

/**
 * Enum representing selectable pet species.
 */
enum class Species { DOG, CAT, OTHER }

/**
 * Holds user input for the pet form.
 */
data class PetFormState(
    val name: String = "",
    val species: Species = Species.DOG,
    val breed: String = "",
    val birthday: LocalDate? = null,
    val weight: String = "",
    val photoUri: Uri? = null,
    val photoBitmap: Bitmap? = null,
    val notes: String = "",
    val nameError: Boolean = false,
)

/**
 * Common ViewModel logic shared between add and edit screens.
 */
open class PetFormViewModel(
    private val repo: InMemoryPetRepository,
) : ViewModel() {

    /** Current state of the form. */
    var state by mutableStateOf(PetFormState())
        protected set

    fun onNameChange(v: String) {
        state = state.copy(name = v, nameError = v.isBlank())
    }

    fun onSpeciesChange(v: Species) {
        state = state.copy(species = v)
    }

    fun onBreedChange(v: String) {
        state = state.copy(breed = v)
    }

    fun onBirthdayChange(v: LocalDate?) {
        state = state.copy(birthday = v)
    }

    fun onWeightChange(v: String) {
        state = state.copy(weight = v)
    }

    fun onPhotoUriChange(uri: Uri?) {
        state = state.copy(photoUri = uri, photoBitmap = null)
    }

    fun onPhotoBitmapChange(bitmap: Bitmap?) {
        state = state.copy(photoBitmap = bitmap, photoUri = null)
    }

    fun onNotesChange(v: String) {
        state = state.copy(notes = v)
    }
}

/**
 * Reusable form composable allowing users to enter pet details.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetForm(
    state: PetFormState,
    onNameChange: (String) -> Unit,
    onSpeciesChange: (Species) -> Unit,
    onBreedChange: (String) -> Unit,
    onBirthdayChange: (LocalDate?) -> Unit,
    onWeightChange: (String) -> Unit,
    onPhotoFromGallery: (Uri?) -> Unit,
    onPhotoFromCamera: (Bitmap?) -> Unit,
    onNotesChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    var showDatePicker by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        onPhotoFromGallery(uri)
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp ->
        onPhotoFromCamera(bmp)
    }

    val dateFormatter = remember { DateTimeFormatter.ISO_LOCAL_DATE }

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = state.name,
            onValueChange = onNameChange,
            label = { Text("Name*") },
            isError = state.nameError,
            modifier = Modifier.fillMaxWidth()
        )
        if (state.nameError) {
            Text("Name is required", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Text("Species")
        SingleChoiceSegmentedButtonRow {
            Species.values().forEachIndexed { index, type ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index, Species.values().size),
                    checked = state.species == type,
                    onCheckedChange = { onSpeciesChange(type) }
                ) { Text(type.name) }
            }
        }

        OutlinedTextField(
            value = state.breed,
            onValueChange = onBreedChange,
            label = { Text("Breed") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.birthday?.format(dateFormatter) ?: "",
            onValueChange = {},
            label = { Text("Birthday") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true }
        )

        if (showDatePicker) {
            val pickerState = rememberDatePickerState(
                initialSelectedDateMillis = state.birthday?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
            )
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val millis = pickerState.selectedDateMillis
                        if (millis != null) {
                            val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                            onBirthdayChange(date)
                        }
                        showDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = pickerState)
            }
        }

        OutlinedTextField(
            value = state.weight,
            onValueChange = onWeightChange,
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Text("Photo")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { galleryLauncher.launch("image/*") }) { Text("Gallery") }
            Button(onClick = { cameraLauncher.launch(null) }) { Text("Camera") }
        }
        state.photoBitmap?.let { bmp ->
            Image(bmp.asImageBitmap(), contentDescription = null, modifier = Modifier.size(120.dp))
        } ?: state.photoUri?.let {
            Text("Photo selected")
        }

        OutlinedTextField(
            value = state.notes,
            onValueChange = onNotesChange,
            label = { Text("Notes") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp)
        )

        Button(
            onClick = onSave,
            enabled = state.name.isNotBlank(),
            modifier = Modifier.align(Alignment.End)
        ) { Text("Save") }
    }
}
