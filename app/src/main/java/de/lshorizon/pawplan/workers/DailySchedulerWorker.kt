package de.lshorizon.pawplan.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import de.lshorizon.pawplan.domain.routines.ListDueRoutines
import de.lshorizon.pawplan.notifications.Scheduler
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Worker that sweeps upcoming routines and schedules their notifications.
 */
class DailySchedulerWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    // Placeholder instances; replace with real dependency injection later.
    private val scheduler = Scheduler(appContext)
    private val listDueRoutines: ListDueRoutines = { _ -> emptyList() }

    override suspend fun doWork(): Result {
        val until = Instant.now().plus(3, ChronoUnit.DAYS)
        val routines = listDueRoutines(until)
        routines.forEach { scheduler.scheduleNotification(it.id, it.dueAt) }
        return Result.success()
    }
}
