package de.lshorizon.pawplan.ui.screen.pets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.lshorizon.pawplan.core.domain.model.Pet

/**
 * Screen displaying all pets with actions to add or edit them.
 */
@Composable
fun PetsScreen(
    viewModel: PetsViewModel = viewModel(),
    onAddPet: () -> Unit = {},
    onEditPet: (Long) -> Unit = {},
) {
    val pets by viewModel.pets.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPet) {
                Icon(Icons.Default.Add, contentDescription = "Add pet")
            }
        }
    ) { padding ->
        if (pets.isEmpty()) {
            EmptyState(Modifier.padding(padding), onAddPet)
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(pets) { pet ->
                    PetRow(pet, onClick = { onEditPet(pet.id) })
                }
            }
        }
    }
}

/**
 * Displays information for a single pet entry.
 */
@Composable
private fun PetRow(pet: Pet, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Pets, contentDescription = null, modifier = Modifier.size(40.dp))
        Spacer(Modifier.width(16.dp))
        Column {
            Text(pet.name, style = MaterialTheme.typography.titleMedium)
            // Placeholder text as species/breed are not yet modeled
            Text("Unknown species", style = MaterialTheme.typography.bodySmall)
        }
    }
}

/**
 * Shown when there are no pets yet, offering to add one.
 */
@Composable
private fun EmptyState(modifier: Modifier = Modifier, onAddPet: () -> Unit) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No pets yet")
            Spacer(Modifier.height(8.dp))
            Button(onClick = onAddPet) {
                Text("Add Pet")
            }
        }
    }
}
