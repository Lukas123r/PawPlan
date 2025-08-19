// core/data/src/main/kotlin/de/lshorizon/pawplan/core/data/di/DataStoreModule.kt
package de.lshorizon.pawplan.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Provides the Preferences DataStore used across the app. */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
  @Provides
  @Singleton
  fun providePreferencesDataStore(
    @ApplicationContext ctx: Context
  ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
    produceFile = { ctx.preferencesDataStoreFile("app_preferences") }
  )
}
