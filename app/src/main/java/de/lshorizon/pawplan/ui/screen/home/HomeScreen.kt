// Opt in to Material3 experimental APIs like ModalBottomSheet.
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package de.lshorizon.pawplan.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter

/**
 * Main home screen listing due routines.
 */
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var snoozeId by remember { mutableStateOf<Long?>(null) }

    // Listen for snackbar events from viewmodel
    LaunchedEffect(Unit) {
        viewModel.snackbar.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Bottom sheet for snooze selection
    if (snoozeId != null) {
        ModalBottomSheet(onDismissRequest = { snoozeId = null }) {
            Column(Modifier.padding(16.dp)) {
                listOf(6L, 24L, 48L).forEach { hours ->
                    TextButton(onClick = {
                        viewModel.snooze(snoozeId!!, hours)
                        snoozeId = null
                    }) { Text("Snooze ${hours}h") }
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item { Text("Due Now", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp)) }
            items(state.dueNow) { item ->
                RoutineCard(item, onDone = { viewModel.markDone(item.id) }, onSnooze = { snoozeId = item.id })
            }
            item { Text("Coming Up", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp)) }
            items(state.comingUp) { item ->
                RoutineCard(item, onDone = { viewModel.markDone(item.id) }, onSnooze = { snoozeId = item.id })
            }
        }
    }
}

/**
 * Displays details for a single routine.
 */
@Composable
fun RoutineCard(item: RoutineItem, onDone: () -> Unit, onSnooze: () -> Unit) {
    val formatter = remember { DateTimeFormatter.ofPattern("MMM d, HH:mm") }
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icon shows which pet the routine belongs to
                Icon(Icons.Outlined.Pets, contentDescription = null, modifier = Modifier.size(40.dp))
                Spacer(Modifier.size(16.dp))
                Column(Modifier.weight(1f)) {
                    Text(item.title, style = MaterialTheme.typography.titleMedium)
                    Text(item.intervalText, style = MaterialTheme.typography.bodySmall)
                    Text("Due ${item.due.format(formatter)}", style = MaterialTheme.typography.bodySmall)
                }
            }
            Row(
                Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDone) { Text("Done") }
                TextButton(onClick = onSnooze) { Text("Snooze") }
            }
        }
    }
}
