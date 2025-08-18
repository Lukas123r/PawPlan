package de.lshorizon.pawplan.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import de.lshorizon.pawplan.core.data.analytics.NoOpAnalytics
import de.lshorizon.pawplan.core.data.repo.InMemoryEventLogRepository
import de.lshorizon.pawplan.core.data.repo.InMemoryRoutineRepository
import de.lshorizon.pawplan.core.domain.usecase.MarkRoutineDone
import de.lshorizon.pawplan.core.domain.usecase.SnoozeRoutine
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
        // use simple repositories to update routine state
        val useCase = MarkRoutineDone(routineRepo, eventRepo, analytics)
        useCase(id.toLong(), java.time.LocalDateTime.now())
    }

    private suspend fun snooze(id: String) {
        // default snooze duration of 24h
        val useCase = SnoozeRoutine(routineRepo, eventRepo, analytics)
        useCase(id.toLong(), 24)
    }

    companion object {
        const val KEY_ROUTINE_ID = "routineId"
        const val KEY_ACTION = "action"
        // repositories for test injection
        var routineRepo: InMemoryRoutineRepository = InMemoryRoutineRepository()
        var eventRepo: InMemoryEventLogRepository = InMemoryEventLogRepository()
        var analytics = NoOpAnalytics()
    }
}
