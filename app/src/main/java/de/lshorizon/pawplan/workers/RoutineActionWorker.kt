package de.lshorizon.pawplan.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import de.lshorizon.pawplan.notifications.RoutineNotificationWorker

/**
 * Worker handling actions from routine notifications like done or snooze.
 */
class RoutineActionWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val id = inputData.getString(KEY_ROUTINE_ID) ?: return Result.failure()
        when (inputData.getString(KEY_ACTION)) {
            RoutineNotificationWorker.ACTION_DONE -> markDone(id)
            RoutineNotificationWorker.ACTION_SNOOZE -> snooze(id)
        }
        return Result.success()
    }

    private suspend fun markDone(id: String) {
        // TODO: invoke MarkRoutineDone use case
    }

    private suspend fun snooze(id: String) {
        // TODO: invoke SnoozeRoutine use case with 24h
    }

    companion object {
        const val KEY_ROUTINE_ID = "routineId"
        const val KEY_ACTION = "action"
    }
}
