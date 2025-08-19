package de.lshorizon.pawplan.core.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** DataStore-backed implementation of [PrefsRepository]. */
@Singleton
class PrefsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PrefsRepository {
    // Key storing whether the user finished the onboarding.
    private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_completed")

    override suspend fun hasSeenOnboarding(): Boolean =
        dataStore.data.map { it[ONBOARDING_KEY] ?: false }.first()

    override suspend fun setOnboardingDone(value: Boolean) {
        dataStore.edit { it[ONBOARDING_KEY] = value }
    }
}
