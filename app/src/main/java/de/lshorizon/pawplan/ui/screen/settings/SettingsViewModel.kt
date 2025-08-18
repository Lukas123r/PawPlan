package de.lshorizon.pawplan.ui.screen.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.lshorizon.pawplan.data.settings.SettingsRepository
import de.lshorizon.pawplan.notifications.enqueueDailyScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.work.WorkManager

/**
 * Bridges [SettingsRepository] with the UI and handles rescheduling.
 */
class SettingsViewModel(
    app: Application,
    private val repo: SettingsRepository = SettingsRepository(app.applicationContext),
) : AndroidViewModel(app) {

    private val context = app.applicationContext

    /** Exposes current preference values to the screen. */
    val state: StateFlow<SettingsState> = combine(
        repo.language,
        repo.notifications,
        repo.remindOverdue,
        repo.plannerHour,
        repo.theme
    ) { lang, notif, remind, hour, theme ->
        SettingsState(lang, notif, remind, hour, theme)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SettingsState())

    fun setLanguage(lang: String) = viewModelScope.launch { repo.setLanguage(lang) }

    fun setNotifications(enabled: Boolean) = viewModelScope.launch {
        repo.setNotifications(enabled)
        reschedule(enabled, state.value.remindOverdue, state.value.plannerHour)
    }

    fun setRemindOverdue(enabled: Boolean) = viewModelScope.launch {
        repo.setRemindOverdue(enabled)
        reschedule(state.value.notifications, enabled, state.value.plannerHour)
    }

    fun setPlannerHour(hour: Int) = viewModelScope.launch {
        repo.setPlannerHour(hour)
        reschedule(state.value.notifications, state.value.remindOverdue, hour)
    }

    fun setTheme(theme: String) = viewModelScope.launch { repo.setTheme(theme) }

    /** Reschedule background work whenever reminder parameters change. */
    private fun reschedule(notifications: Boolean, remind: Boolean, hour: Int) {
        if (notifications) {
            // Re-enqueue daily planner sweep when notifications are enabled
            enqueueDailyScheduler(context, hour)
        } else {
            WorkManager.getInstance(context).cancelAllWork()
        }
    }
}

/** Holds current settings for the screen. */
data class SettingsState(
    val language: String = "en",
    val notifications: Boolean = true,
    val remindOverdue: Boolean = true,
    val plannerHour: Int = 9,
    val theme: String = "system",
)
