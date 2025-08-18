package de.lshorizon.pawplan.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Settings screen showing common preferences like language and theme.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
    onExport: () -> Unit = {},
    onImport: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Text("General", style = MaterialTheme.typography.titleMedium) }
            item { LanguageSetting(state.language, viewModel::setLanguage) }
            item { ToggleSetting("Notifications", state.notifications, viewModel::setNotifications) }
            item { ToggleSetting("Remind overdue again", state.remindOverdue, viewModel::setRemindOverdue) }
            item { PlannerHourSetting(state.plannerHour, viewModel::setPlannerHour) }
            item { ThemeSetting(state.theme, viewModel::setTheme) }
            item {
                Text("Data", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = onExport) { Text("Export") }
                    Button(onClick = onImport) { Text("Import") }
                }
            }
            item {
                Text("Legal", style = MaterialTheme.typography.titleMedium)
                Text("Privacy Policy", color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable { })
                Text("Terms of Service", color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable { })
            }
        }
    }
}

/** Switch row used for boolean preferences. */
@Composable
private fun ToggleSetting(label: String, value: Boolean, onChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = value, onCheckedChange = onChange)
    }
}

/** Dropdown for choosing language. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageSetting(current: String, onChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val label = if (current == "de") "Deutsch" else "English"
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = label,
            onValueChange = {},
            label = { Text("Language") },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Deutsch") }, onClick = { onChange("de"); expanded = false })
            DropdownMenuItem(text = { Text("English") }, onClick = { onChange("en"); expanded = false })
        }
    }
}

/** Dropdown for choosing planner sweep hour. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlannerHourSetting(hour: Int, onChange: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = String.format("%02d:00", hour),
            onValueChange = {},
            label = { Text("Daily planner time") },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            (0..23).forEach { h ->
                DropdownMenuItem(text = { Text(String.format("%02d:00", h)) }, onClick = { onChange(h); expanded = false })
            }
        }
    }
}

/** Dropdown for selecting visual theme. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeSetting(theme: String, onChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val label = when (theme) {
        "light" -> "Light"
        "dark" -> "Dark"
        else -> "System"
    }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = label,
            onValueChange = {},
            label = { Text("Theme") },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("System") }, onClick = { onChange("system"); expanded = false })
            DropdownMenuItem(text = { Text("Light") }, onClick = { onChange("light"); expanded = false })
            DropdownMenuItem(text = { Text("Dark") }, onClick = { onChange("dark"); expanded = false })
        }
    }
}
