package de.lshorizon.pawplan

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main application class that boots Hilt for dependency injection.
 */
@HiltAndroidApp
class PawPlanApp : Application()

