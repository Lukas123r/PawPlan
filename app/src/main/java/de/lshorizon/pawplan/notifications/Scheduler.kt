package de.lshorizon.pawplan.notifications

import android.content.Context
import android.os.Build
import androidx.work.*
import de.lshorizon.pawplan.workers.DailySchedulerWorker
import java.time.Duration
import java.time.Instant
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Schedules recurring and one-off notifications using WorkManager.
 */

// Berechnet Minuten bis zur nächsten Ausführung um [hour]:00 (lokal)
private fun computeInitialDelayMinutes(hour: Int): Long {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        @Suppress("NewApi")
        run {
            val now = java.time.ZonedDateTime.now()
            val next0 = now.withHour(hour).withMinute(0).withSecond(0).withNano(0)
            val next = if (next0.isBefore(now)) next0.plusDays(1) else next0
            java.time.Duration.between(now, next).toMinutes()
        }
    } else {
        val now = Calendar.getInstance()
        val next = now.clone() as Calendar
        next.set(Calendar.HOUR_OF_DAY, hour)
        next.set(Calendar.MINUTE, 0)
        next.set(Calendar.SECOND, 0)
        next.set(Calendar.MILLISECOND, 0)
        if (next.before(now)) next.add(Calendar.DAY_OF_YEAR, 1)
        TimeUnit.MILLISECONDS.toMinutes(next.timeInMillis - now.timeInMillis)
    }
}

/**
 * Plan daily worker around the provided hour.
 */
fun enqueueDailyScheduler(context: Context, hour: Int = 4) {
    val delayMin = computeInitialDelayMinutes(hour).coerceAtLeast(0)
    val request = PeriodicWorkRequestBuilder<DailySchedulerWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(delayMin, TimeUnit.MINUTES)
        .build()
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "daily-scheduler", ExistingPeriodicWorkPolicy.UPDATE, request
    )
}

/**
 * Schedule a notification for the given routine at the due time.
 */
fun scheduleNotification(context: Context, routineId: String, dueAt: Instant) {
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
    WorkManager.getInstance(context).cancelAllWorkByTag(routineId)
}
