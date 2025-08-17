package de.lshorizon.pawplan.data.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit

/**
 * Stores and exposes user settings via Jetpack DataStore.
 */
class SettingsRepository(private val context: Context) {
    private val Context.dataStore by preferencesDataStore("settings")

    private val LANGUAGE = stringPreferencesKey("language")
    private val NOTIFICATIONS = booleanPreferencesKey("notifications")
    private val REMIND_OVERDUE = booleanPreferencesKey("remind_overdue")
    private val PLANNER_HOUR = intPreferencesKey("planner_hour")
    private val THEME = stringPreferencesKey("theme")

    /** Current language code like "de" or "en". */
    val language: Flow<String> = context.dataStore.data.map { it[LANGUAGE] ?: "en" }
    /** Whether notifications are globally enabled. */
    val notifications: Flow<Boolean> = context.dataStore.data.map { it[NOTIFICATIONS] ?: true }
    /** Toggles reminding overdue routines again. */
    val remindOverdue: Flow<Boolean> = context.dataStore.data.map { it[REMIND_OVERDUE] ?: true }
    /** Preferred hour of daily planner sweep. */
    val plannerHour: Flow<Int> = context.dataStore.data.map { it[PLANNER_HOUR] ?: 9 }
    /** Selected theme mode. */
    val theme: Flow<String> = context.dataStore.data.map { it[THEME] ?: "system" }

    suspend fun setLanguage(value: String) { context.dataStore.edit { it[LANGUAGE] = value } }
    suspend fun setNotifications(value: Boolean) { context.dataStore.edit { it[NOTIFICATIONS] = value } }
    suspend fun setRemindOverdue(value: Boolean) { context.dataStore.edit { it[REMIND_OVERDUE] = value } }
    suspend fun setPlannerHour(value: Int) { context.dataStore.edit { it[PLANNER_HOUR] = value } }
    suspend fun setTheme(value: String) { context.dataStore.edit { it[THEME] = value } }
}
