package de.lshorizon.pawplan.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import de.lshorizon.pawplan.domain.routines.ListDueRoutines

/**
 * Worker that scans upcoming routines and schedules notifications.
 */
@HiltWorker
class DailySchedulerWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val listDueRoutines: ListDueRoutines // Use case to fetch due routines
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        // TODO: horizon=3 Tage berechnen und Notifications planen
        return Result.success()
    }
}
