package de.lshorizon.pawplan.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

/**
 * Helper class to schedule sweeps and individual routine notifications.
 */
class Scheduler(private val context: Context) {

    private val workManager get() = WorkManager.getInstance(context)

    /**
     * Schedule a daily sweep worker around the provided hour.
     */
    fun scheduleDailySweep(hour: Int = 4) {
        val now = ZonedDateTime.now()
        var next = now.withHour(hour).withMinute(0).withSecond(0).withNano(0)
        if (now >= next) next = next.plusDays(1)
        val delay = Duration.between(now, next)
        val request = PeriodicWorkRequestBuilder<DailySchedulerWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delay.toMillis(), TimeUnit.MILLISECONDS)
            .build()
        workManager.enqueueUniquePeriodicWork(
            DAILY_SWEEP_WORK,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    /**
     * Schedule a notification for the given routine at the due time.
     */
    fun scheduleNotification(routineId: String, dueAt: Instant) {
        val delay = Duration.between(Instant.now(), dueAt).let { if (it.isNegative) Duration.ZERO else it }
        val data = workDataOf(
            RoutineNotificationWorker.KEY_ROUTINE_ID to routineId,
            RoutineNotificationWorker.KEY_DUE_AT to dueAt.toEpochMilli()
        )
        val request = OneTimeWorkRequestBuilder<RoutineNotificationWorker>()
            .setInitialDelay(delay.toMillis(), TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag(routineId)
            .build()
        val name = "notify_${routineId}_${dueAt.toEpochMilli()}"
        workManager.enqueueUniqueWork(name, ExistingWorkPolicy.REPLACE, request)
    }

    /**
     * Cancel all scheduled work for a specific routine.
     */
    fun cancelForRoutine(routineId: String) {
        workManager.cancelAllWorkByTag(routineId)
    }

    private companion object {
        const val DAILY_SWEEP_WORK = "daily_scheduler_sweep"
    }
}
