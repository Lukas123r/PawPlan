package de.lshorizon.pawplan.workers

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Data
import androidx.work.testing.TestListenableWorkerBuilder
import de.lshorizon.pawplan.core.data.repo.InMemoryEventLogRepository
import de.lshorizon.pawplan.core.data.repo.InMemoryRoutineRepository
import de.lshorizon.pawplan.core.domain.model.Routine
import de.lshorizon.pawplan.notifications.RoutineNotificationWorker
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import java.time.LocalDateTime

/**
 * Ensures routine actions via worker update in-memory repositories.
 */
@RunWith(AndroidJUnit4::class)
class RoutineActionWorkerTest {
    @Test
    fun `done action marks routine finished`() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val routineRepo = InMemoryRoutineRepository()
        val eventRepo = InMemoryEventLogRepository()
        val routine = runBlocking {
            routineRepo.addRoutine(Routine(petId = 1, name = "Test", intervalDays = 1))
        }
        RoutineActionWorker.routineRepo = routineRepo
        RoutineActionWorker.eventRepo = eventRepo
        val input = Data.Builder()
            .putString(RoutineActionWorker.KEY_ROUTINE_ID, routine.id.toString())
            .putString(RoutineActionWorker.KEY_ACTION, RoutineNotificationWorker.ACTION_DONE)
            .build()
        val worker = TestListenableWorkerBuilder<RoutineActionWorker>(context)
            .setInputData(input)
            .build()
        runBlocking { worker.startWork().await() }
        val updated = runBlocking { routineRepo.getRoutine(routine.id) }
        assertNotNull(updated?.lastDone)
    }

    @Test
    fun `snooze action postpones routine`() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val routineRepo = InMemoryRoutineRepository()
        val eventRepo = InMemoryEventLogRepository()
        val routine = runBlocking {
            routineRepo.addRoutine(Routine(petId = 1, name = "Test", intervalDays = 1, lastDone = LocalDateTime.now()))
        }
        RoutineActionWorker.routineRepo = routineRepo
        RoutineActionWorker.eventRepo = eventRepo
        val input = Data.Builder()
            .putString(RoutineActionWorker.KEY_ROUTINE_ID, routine.id.toString())
            .putString(RoutineActionWorker.KEY_ACTION, RoutineNotificationWorker.ACTION_SNOOZE)
            .build()
        val worker = TestListenableWorkerBuilder<RoutineActionWorker>(context)
            .setInputData(input)
            .build()
        runBlocking { worker.startWork().await() }
        val updated = runBlocking { routineRepo.getRoutine(routine.id) }
        assertTrue(updated?.snoozedUntil != null)
    }
}
