package de.lshorizon.pawplan.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import de.lshorizon.pawplan.notifications.RoutineNotificationWorker
import de.lshorizon.pawplan.workers.RoutineActionWorker

/**
 * Receives notification actions and forwards them to a worker.
 */
class RoutineActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getStringExtra(RoutineNotificationWorker.EXTRA_ROUTINE_ID) ?: return
        val action = intent.action ?: return
        val data = Data.Builder()
            .putString(RoutineActionWorker.KEY_ROUTINE_ID, id)
            .putString(RoutineActionWorker.KEY_ACTION, action)
            .build()
        val request = OneTimeWorkRequestBuilder<RoutineActionWorker>()
            .setInputData(data)
            .build()
        WorkManager.getInstance(context).enqueue(request)
    }
}
