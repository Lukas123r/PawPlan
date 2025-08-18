// Opt in to Material3 experimental APIs for the exposed dropdown.
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package de.lshorizon.pawplan.ui.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Minimal settings screen showing a language selector.
 */
@Composable
fun SettingsScreen() {
    var langExpanded by remember { mutableStateOf(false) }
    var selectedLang by remember { mutableStateOf("Deutsch") }

    Column(Modifier.padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        Text("Language", style = MaterialTheme.typography.labelMedium)
        ExposedDropdownMenuBox(
            expanded = langExpanded,
            onExpandedChange = { langExpanded = !langExpanded },
        ) {
            TextField(
                value = selectedLang,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = langExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = langExpanded,
                onDismissRequest = { langExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Deutsch") },
                    onClick = { selectedLang = "Deutsch"; langExpanded = false }
                )
                DropdownMenuItem(
                    text = { Text("English") },
                    onClick = { selectedLang = "English"; langExpanded = false }
                )
            }
        }
    }
}
