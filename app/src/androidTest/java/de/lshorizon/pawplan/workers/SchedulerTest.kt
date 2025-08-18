package de.lshorizon.pawplan.workers

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.WorkManager
import androidx.work.testing.WorkManagerTestInitHelper
import de.lshorizon.pawplan.notifications.enqueueDailyScheduler
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.runner.RunWith

/**
 * Confirms that the daily sweep work is scheduled once.
 */
@RunWith(AndroidJUnit4::class)
class SchedulerTest {
    @Test
    fun `daily sweep enqueues periodic work`() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
        // Enqueue a daily sweep at 1 AM for testing
        enqueueDailyScheduler(context, 1)
        val workInfos = WorkManager.getInstance(context)
            .getWorkInfosForUniqueWork("daily_scheduler_sweep").get()
        assertEquals(1, workInfos.size)
    }
}
