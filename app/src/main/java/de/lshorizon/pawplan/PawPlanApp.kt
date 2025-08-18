package de.lshorizon.pawplan

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/** Application class connecting WorkManager with Hilt's worker factory. */
@HiltAndroidApp
class PawPlanApp : Application(), Configuration.Provider {
    // Inject the factory so workers can use Hilt for dependencies
    @Inject lateinit var workerFactory: HiltWorkerFactory

    // Supply WorkManager with the injected factory
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}

