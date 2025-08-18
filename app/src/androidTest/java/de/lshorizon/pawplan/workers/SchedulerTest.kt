package de.lshorizon.pawplan.workers

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.WorkManager
import androidx.work.testing.WorkManagerTestInitHelper
import de.lshorizon.pawplan.notifications.Scheduler
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
        val scheduler = Scheduler(context)
        scheduler.scheduleDailySweep(1)
        val workInfos = WorkManager.getInstance(context)
            .getWorkInfosForUniqueWork("daily_scheduler_sweep").get()
        assertEquals(1, workInfos.size)
    }
}
