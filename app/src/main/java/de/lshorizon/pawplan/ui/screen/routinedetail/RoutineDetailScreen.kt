package de.lshorizon.pawplan.ui.screen.routinedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import de.lshorizon.pawplan.core.data.analytics.NoOpAnalytics
import de.lshorizon.pawplan.core.data.repo.InMemoryEventLogRepository
import de.lshorizon.pawplan.core.data.repo.InMemoryPetRepository
import de.lshorizon.pawplan.core.data.repo.InMemoryRoutineRepository
import de.lshorizon.pawplan.core.domain.model.EventLog
import de.lshorizon.pawplan.core.domain.model.Routine
import de.lshorizon.pawplan.core.domain.usecase.GetHistoryByRoutine
import de.lshorizon.pawplan.core.domain.usecase.MarkRoutineDone
import de.lshorizon.pawplan.core.domain.usecase.SnoozeRoutine
import de.lshorizon.pawplan.core.domain.usecase.ListPets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Detail view for a single routine showing meta data and action history.
 */
@Composable
fun RoutineDetailScreen(
    id: Long,
    viewModel: RoutineDetailViewModel = viewModel(factory = RoutineDetailViewModel.Factory(id)),
) {
    val state by viewModel.state.collectAsState()
    val formatter = DateTimeFormatter.ofPattern("MMM d, HH:mm")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(state.petName, style = MaterialTheme.typography.titleMedium)
                Text(state.title, style = MaterialTheme.typography.headlineSmall)
                Text(state.intervalText, style = MaterialTheme.typography.bodyMedium)
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Next due ${state.nextDue.format(formatter)}",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { viewModel.markDone() }) { Text("Mark done") }
                listOf(6L, 24L, 48L).forEach { h ->
                    Button(onClick = { viewModel.snooze(h) }) { Text("Snooze ${h}h") }
                }
            }
        }
        item { Text("History", style = MaterialTheme.typography.titleMedium) }
        items(state.history) { event ->
            Text(
                "${event.type} – ${event.timestamp.format(formatter)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/** Holds all data required for [RoutineDetailScreen]. */
data class RoutineDetailState(
    val petName: String = "",
    val title: String = "",
    val intervalText: String = "",
    val nextDue: LocalDateTime = LocalDateTime.now(),
    val history: List<EventLog> = emptyList(),
)

/** Handles routine actions and exposes its state. */
class RoutineDetailViewModel(
    private val id: Long,
    private val routineRepo: InMemoryRoutineRepository = InMemoryRoutineRepository(),
    private val petRepo: InMemoryPetRepository = InMemoryPetRepository(),
    private val eventRepo: InMemoryEventLogRepository = InMemoryEventLogRepository(),
) : ViewModel() {

    private val markDone = MarkRoutineDone(routineRepo, eventRepo, NoOpAnalytics()) // analytics event
    private val snooze = SnoozeRoutine(routineRepo, eventRepo, NoOpAnalytics()) // analytics event
    private val history = GetHistoryByRoutine(eventRepo)
    private val listPets = ListPets(petRepo)

    private val routineFlow: Flow<Routine?> =
        routineRepo.getRoutines().map { list -> list.find { it.id == id } }

    /** State exposed to the UI. */
    val state: StateFlow<RoutineDetailState> = combine(
        routineFlow,
        listPets(),
        history(id)
    ) { routine, pets, events ->
        if (routine == null) RoutineDetailState() else {
            val pet = pets.find { it.id == routine.petId }
            val due = computeDueTime(routine)
            RoutineDetailState(
                petName = pet?.name ?: "",
                title = routine.name,
                intervalText = "Every ${routine.intervalDays} days",
                nextDue = due,
                history = events.sortedByDescending { it.timestamp }
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, RoutineDetailState())

    /** Calculates the next time the routine should run. */
    private fun computeDueTime(routine: Routine): LocalDateTime {
        val last = routine.lastDone ?: LocalDateTime.MIN
        val dueTime = last.plusDays(routine.intervalDays.toLong())
        val snoozed = routine.snoozedUntil ?: dueTime
        return if (snoozed.isAfter(dueTime)) snoozed else dueTime
    }

    fun markDone() {
        viewModelScope.launch { markDone(id, LocalDateTime.now()) }
    }

    fun snooze(hours: Long) {
        viewModelScope.launch { snooze(id, hours) }
    }

    /** Factory to create [RoutineDetailViewModel] with routine id. */
    class Factory(private val routineId: Long) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return RoutineDetailViewModel(routineId) as T
        }
    }
}
