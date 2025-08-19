// core/data/src/main/kotlin/de/lshorizon/pawplan/core/data/onboarding/OnboardingRepositoryImpl.kt
package de.lshorizon.pawplan.core.data.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** Concrete implementation storing completion state in DataStore. */
@Singleton
class OnboardingRepositoryImpl @Inject constructor(
  private val dataStore: DataStore<Preferences>
) : OnboardingRepository {
  private val KEY = booleanPreferencesKey("onboarding_completed")

  override suspend fun isCompleted(): Boolean =
    dataStore.data.map { it[KEY] ?: false }.first()

  override suspend fun setCompleted(value: Boolean) {
    dataStore.edit { it[KEY] = value }
  }
}
