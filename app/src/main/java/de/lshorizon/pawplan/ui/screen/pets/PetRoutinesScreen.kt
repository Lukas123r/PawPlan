// Opt in to Material3 experimental APIs used by top app bars.
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package de.lshorizon.pawplan.ui.screen.pets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import de.lshorizon.pawplan.core.data.repo.InMemoryRoutineRepository
import de.lshorizon.pawplan.core.domain.model.Routine
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/** Displays all active routines for a specific pet. */
@Composable
fun PetRoutinesScreen(
    petId: Long,
    viewModel: PetRoutinesViewModel = viewModel(factory = PetRoutinesViewModel.Factory(petId)),
) {
    val routines by viewModel.routines.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Routines") }) }) { padding ->
        if (routines.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) { Text("No active routines") }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(routines) { item -> RoutineRow(item) }
            }
        }
    }
}

/** Simple row displaying routine name and due date. */
@Composable
private fun RoutineRow(item: RoutineItem) {
    val formatter = DateTimeFormatter.ofPattern("MMM d")
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(item.name, style = MaterialTheme.typography.titleMedium)
        Text("Due ${item.nextDue.format(formatter)}", style = MaterialTheme.typography.bodySmall)
    }
}

/** Represents a routine entry in the list. */
data class RoutineItem(val id: Long, val name: String, val nextDue: LocalDateTime)

/** ViewModel filtering routines for a single pet. */
class PetRoutinesViewModel(
    private val petId: Long,
    private val repo: InMemoryRoutineRepository = InMemoryRoutineRepository(),
) : ViewModel() {

    /** Stream of routines belonging to [petId]. */
    val routines: StateFlow<List<RoutineItem>> = repo.getRoutines().map { list ->
        list.filter { it.petId == petId && it.active }.map { r ->
            val due = computeDue(r)
            RoutineItem(r.id, r.name, due)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private fun computeDue(routine: Routine): LocalDateTime {
        val last = routine.lastDone ?: LocalDateTime.MIN
        val due = last.plusDays(routine.intervalDays.toLong())
        val snoozed = routine.snoozedUntil ?: due
        return if (snoozed.isAfter(due)) snoozed else due
    }

    companion object {
        /** Factory to construct a [PetRoutinesViewModel] with [petId]. */
        fun Factory(id: Long) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PetRoutinesViewModel(id) as T
            }
        }
    }
}

