package de.lshorizon.pawplan.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import de.lshorizon.pawplan.workers.DailySchedulerWorker
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

/**
 * Schedules recurring and one-off notifications using WorkManager.
 */
fun enqueueDailyScheduler(context: Context, hour: Int = 4) {
    // Schedule a daily sweep worker around the provided hour
    val now = ZonedDateTime.now()
    val next = now.withHour(hour).withMinute(0).withSecond(0).withNano(0)
        .let { if (it.isBefore(now)) it.plusDays(1) else it }
    val minutes = Duration.between(now, next).toMinutes()
    val request = PeriodicWorkRequestBuilder<DailySchedulerWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(minutes, TimeUnit.MINUTES)
        .build()
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "daily-scheduler", ExistingPeriodicWorkPolicy.UPDATE, request
    )
}

/**
 * Schedule a notification for the given routine at the due time.
 */
fun scheduleNotification(context: Context, routineId: String, dueAt: Instant) {
    // Plan a one-time work request for routine notification
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
    WorkManager.getInstance(context).enqueueUniqueWork(name, ExistingWorkPolicy.REPLACE, request)
}

/**
 * Cancel all scheduled work for a specific routine.
 */
fun cancelForRoutine(context: Context, routineId: String) {
    // Remove any pending notifications for the routine
    WorkManager.getInstance(context).cancelAllWorkByTag(routineId)
}
