package de.lshorizon.pawplan.ui.screen.addroutine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import de.lshorizon.pawplan.core.data.repo.InMemoryPetRepository
import de.lshorizon.pawplan.core.data.repo.InMemoryRoutineRepository
import de.lshorizon.pawplan.core.domain.model.Pet
import de.lshorizon.pawplan.core.domain.model.Routine
import de.lshorizon.pawplan.core.domain.usecase.CreateRoutine
import de.lshorizon.pawplan.core.domain.usecase.ListPets
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Screen for adding a new routine with a form-based workflow.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoutineScreen(
    viewModel: AddRoutineViewModel = viewModel(),
    onBack: () -> Unit = {},
) {
    val state = viewModel.state
    val pets by viewModel.pets.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Add Routine") }) }) { padding ->
        RoutineForm(
            state = state,
            pets = pets,
            onPetChange = viewModel::onPetChange,
            onTitleChange = viewModel::onTitleChange,
            onCategoryChange = viewModel::onCategoryChange,
            onIntervalSelect = viewModel::onIntervalChange,
            onCustomIntervalChange = viewModel::onCustomIntervalChange,
            onStartDateChange = viewModel::onStartDateChange,
            onNotesChange = viewModel::onNotesChange,
            onSave = { viewModel.save(onBack) },
            modifier = Modifier.padding(padding)
        )
    }
}

/** Represents user input for creating a routine. */
data class RoutineFormState(
    val petId: Long? = null,
    val title: String = "",
    val category: String = "",
    val intervalWeeks: Int = 4,
    val customInterval: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val notes: String = "",
    val titleError: Boolean = false,
    val useCustomInterval: Boolean = false,
)

/** Handles form state and saving logic for [AddRoutineScreen]. */
class AddRoutineViewModel(
    private val routineRepo: InMemoryRoutineRepository = InMemoryRoutineRepository(),
    private val petRepo: InMemoryPetRepository = InMemoryPetRepository(),
) : ViewModel() {

    private val createRoutine = CreateRoutine(routineRepo)
    private val listPets = ListPets(petRepo)

    /** Current form state exposed to the UI. */
    var state by mutableStateOf(RoutineFormState())
        private set

    /** Emits all available pets for the drop-down selector. */
    val pets: StateFlow<List<Pet>> =
        listPets().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onPetChange(id: Long) { state = state.copy(petId = id) }
    fun onTitleChange(v: String) { state = state.copy(title = v, titleError = v.isBlank()) }
    fun onCategoryChange(v: String) { state = state.copy(category = v) }
    fun onIntervalChange(weeks: Int, custom: Boolean) {
        state = state.copy(intervalWeeks = weeks, useCustomInterval = custom)
    }
    fun onCustomIntervalChange(v: String) {
        state = state.copy(customInterval = v)
    }
    fun onStartDateChange(date: LocalDate) { state = state.copy(startDate = date) }
    fun onNotesChange(v: String) { state = state.copy(notes = v) }

    /** Validate and persist the routine. */
    fun save(onSaved: () -> Unit) {
        if (state.title.isBlank() || state.petId == null) {
            state = state.copy(titleError = state.title.isBlank())
            return
        }
        viewModelScope.launch {
            val weeks = if (state.useCustomInterval) {
                state.customInterval.toIntOrNull() ?: state.intervalWeeks
            } else {
                state.intervalWeeks
            }
            val days = weeks * 7
            createRoutine(
                Routine(
                    petId = state.petId!!,
                    name = state.title,
                    intervalDays = days,
                    category = state.category,
                    notes = state.notes,
                    lastDone = state.startDate.atStartOfDay(),
                )
            )
            onSaved()
        }
    }
}

/** Form layout displaying the routine fields. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoutineForm(
    state: RoutineFormState,
    pets: List<Pet>,
    onPetChange: (Long) -> Unit,
    onTitleChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onIntervalSelect: (Int, Boolean) -> Unit,
    onCustomIntervalChange: (String) -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onNotesChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = remember { DateTimeFormatter.ISO_LOCAL_DATE }
    var petExpanded by remember { mutableStateOf(false) }
    var intervalExpanded by remember { mutableStateOf(false) }
    val intervalOptions = listOf(3, 4, 6, 8, 12)

    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Pet selection drop-down
        ExposedDropdownMenuBox(expanded = petExpanded, onExpandedChange = { petExpanded = it }) {
            OutlinedTextField(
                value = pets.firstOrNull { it.id == state.petId }?.name ?: "",
                onValueChange = {},
                label = { Text("Pet") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = petExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = petExpanded, onDismissRequest = { petExpanded = false }) {
                pets.forEach { pet ->
                    DropdownMenuItem(text = { Text(pet.name) }, onClick = {
                        onPetChange(pet.id)
                        petExpanded = false
                    })
                }
            }
        }

        OutlinedTextField(
            value = state.title,
            onValueChange = onTitleChange,
            label = { Text("Title*") },
            isError = state.titleError,
            modifier = Modifier.fillMaxWidth()
        )
        if (state.titleError) {
            Text("Title is required", color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = state.category,
            onValueChange = onCategoryChange,
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth()
        )

        // Interval selection
        ExposedDropdownMenuBox(expanded = intervalExpanded, onExpandedChange = { intervalExpanded = it }) {
            OutlinedTextField(
                value = if (state.useCustomInterval) state.customInterval else "${state.intervalWeeks}",
                onValueChange = {},
                label = { Text("Interval (weeks)") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = intervalExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = intervalExpanded, onDismissRequest = { intervalExpanded = false }) {
                intervalOptions.forEach { w ->
                    DropdownMenuItem(text = { Text("$w") }, onClick = {
                        onIntervalSelect(w, false)
                        intervalExpanded = false
                    })
                }
                DropdownMenuItem(text = { Text("Custom") }, onClick = {
                    onIntervalSelect(state.intervalWeeks, true)
                    intervalExpanded = false
                })
            }
        }
        if (state.useCustomInterval) {
            OutlinedTextField(
                value = state.customInterval,
                onValueChange = onCustomIntervalChange,
                label = { Text("Custom weeks") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        var showDatePicker by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = state.startDate.format(dateFormatter),
            onValueChange = {},
            label = { Text("Start Date") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true }
        )
        if (showDatePicker) {
            val pickerState = rememberDatePickerState(
                initialSelectedDateMillis = state.startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        pickerState.selectedDateMillis?.let {
                            val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                            onStartDateChange(date)
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
            value = state.notes,
            onValueChange = onNotesChange,
            label = { Text("Notes") },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )

        Button(
            onClick = onSave,
            enabled = state.petId != null && state.title.isNotBlank(),
            modifier = Modifier.align(Alignment.End)
        ) { Text("Save") }
    }
}

