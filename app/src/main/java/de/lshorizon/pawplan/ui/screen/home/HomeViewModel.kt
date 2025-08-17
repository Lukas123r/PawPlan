package de.lshorizon.pawplan.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lshorizon.pawplan.core.data.repo.InMemoryEventLogRepository
import de.lshorizon.pawplan.core.data.repo.InMemoryPetRepository
import de.lshorizon.pawplan.core.data.repo.InMemoryRoutineRepository
import de.lshorizon.pawplan.core.domain.model.Pet
import de.lshorizon.pawplan.core.domain.model.Routine
import de.lshorizon.pawplan.core.domain.usecase.ListDueRoutines
import de.lshorizon.pawplan.core.domain.usecase.ListPets
import de.lshorizon.pawplan.core.domain.usecase.MarkRoutineDone
import de.lshorizon.pawplan.core.domain.usecase.SnoozeRoutine
import java.time.LocalDateTime
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Holds routine data for the home screen and handles actions.
 */
class HomeViewModel(
    private val routineRepo: InMemoryRoutineRepository = InMemoryRoutineRepository(),
    private val petRepo: InMemoryPetRepository = InMemoryPetRepository(),
    private val eventRepo: InMemoryEventLogRepository = InMemoryEventLogRepository(),
) : ViewModel() {

    private val listDueRoutines = ListDueRoutines(routineRepo)
    private val listPets = ListPets(petRepo)
    private val markRoutineDone = MarkRoutineDone(routineRepo, eventRepo)
    private val snoozeRoutine = SnoozeRoutine(routineRepo, eventRepo)

    private val _snackbar = MutableSharedFlow<String>()
    val snackbar = _snackbar.asSharedFlow()

    val state: StateFlow<HomeState> = combine(
        listDueRoutines(LocalDateTime.now().plusDays(3)),
        listPets()
    ) { routines, pets ->
        buildState(routines, pets)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, HomeState())

    private fun buildState(routines: List<Routine>, pets: List<Pet>): HomeState {
        val petMap = pets.associateBy { it.id }
        val now = LocalDateTime.now()
        val items = routines.map { r ->
            val due = computeDueTime(r)
            RoutineItem(
                id = r.id,
                title = "${r.name} — ${petMap[r.petId]?.name ?: ""}",
                intervalText = "Every ${r.intervalDays} days",
                due = due
            )
        }
        val (dueNow, comingUp) = items.partition { it.due <= now }
        return HomeState(dueNow, comingUp)
    }

    private fun computeDueTime(routine: Routine): LocalDateTime {
        val last = routine.lastDone ?: LocalDateTime.MIN
        val dueTime = last.plusDays(routine.intervalDays.toLong())
        val snoozed = routine.snoozedUntil ?: dueTime
        return if (snoozed.isAfter(dueTime)) snoozed else dueTime
    }

    fun markDone(id: Long) {
        viewModelScope.launch {
            markRoutineDone(id, LocalDateTime.now())
            _snackbar.emit("Routine completed")
        }
    }

    fun snooze(id: Long, hours: Long) {
        viewModelScope.launch {
            snoozeRoutine(id, hours)
            _snackbar.emit("Snoozed for $hours h")
        }
    }
}

/**
 * Represents a routine entry to display.
 */
data class RoutineItem(
    val id: Long,
    val title: String,
    val intervalText: String,
    val due: LocalDateTime,
)

/**
 * UI state for the home screen.
 */
data class HomeState(
    val dueNow: List<RoutineItem> = emptyList(),
    val comingUp: List<RoutineItem> = emptyList(),
)
