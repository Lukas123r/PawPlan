package de.lshorizon.pawplan.data.onboarding

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Persists whether the user has completed onboarding via DataStore.
 */
class OnboardingRepository(private val context: Context) {
    private val Context.dataStore by preferencesDataStore("onboarding")
    private val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")

    /** Flow emitting true once onboarding was finished. */
    val isOnboardingComplete: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[ONBOARDING_DONE] ?: false
    }

    /** Marks onboarding as completed. */
    suspend fun setCompleted() {
        context.dataStore.edit { it[ONBOARDING_DONE] = true }
    }
}
