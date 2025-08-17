package de.lshorizon.pawplan.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.app.PendingIntent
import android.content.Intent
import de.lshorizon.pawplan.R
import de.lshorizon.pawplan.receivers.RoutineActionReceiver

/**
 * Worker that shows a notification for a routine when it is due.
 */
class RoutineNotificationWorker(
    private val ctx: Context,
    params: WorkerParameters
) : Worker(ctx, params) {

    override fun doWork(): Result {
        val id = inputData.getString(KEY_ROUTINE_ID) ?: return Result.failure()
        createChannel()
        val builder = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(ctx.getString(R.string.app_name))
            .setContentText("Routine $id due")
            .setAutoCancel(true)
            .addAction(buildDoneAction(id))
            .addAction(buildSnoozeAction(id))

        with(NotificationManagerCompat.from(ctx)) {
            notify(id.hashCode(), builder.build())
        }
        return Result.success()
    }

    private fun buildDoneAction(id: String): NotificationCompat.Action {
        val intent = Intent(ctx, RoutineActionReceiver::class.java).apply {
            action = ACTION_DONE
            putExtra(EXTRA_ROUTINE_ID, id)
        }
        val pending = PendingIntent.getBroadcast(
            ctx,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Action(0, ctx.getString(R.string.done), pending)
    }

    private fun buildSnoozeAction(id: String): NotificationCompat.Action {
        val intent = Intent(ctx, RoutineActionReceiver::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(EXTRA_ROUTINE_ID, id)
        }
        val pending = PendingIntent.getBroadcast(
            ctx,
            id.hashCode() + 1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Action(0, ctx.getString(R.string.snooze_24h), pending)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(CHANNEL_ID, "Routines", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val KEY_ROUTINE_ID = "routineId"
        const val KEY_DUE_AT = "dueAt"
        const val CHANNEL_ID = "routine_notifications"
        const val ACTION_DONE = "de.lshorizon.pawplan.action.DONE"
        const val ACTION_SNOOZE = "de.lshorizon.pawplan.action.SNOOZE"
        const val EXTRA_ROUTINE_ID = "extra_routine_id"
    }
}
