package de.lshorizon.pawplan

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application class that connects WorkManager with Hilt.
 * This ensures workers can request dependencies via Hilt.
 */
@HiltAndroidApp
class PawPlanApp : Application(), Configuration.Provider {

    // Injects a factory so WorkManager can create @HiltWorker instances
    @Inject lateinit var workerFactory: HiltWorkerFactory

    // Provide WorkManager configuration using the injected factory
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(workerFactory)
            .build()
}

