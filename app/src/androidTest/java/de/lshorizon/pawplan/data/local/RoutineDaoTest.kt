package de.lshorizon.pawplan.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.lshorizon.pawplan.data.local.dao.RoutineDao
import de.lshorizon.pawplan.data.local.db.AppDatabase
import de.lshorizon.pawplan.data.local.entity.PetEntity
import de.lshorizon.pawplan.data.local.entity.RoutineEntity
import java.time.Instant
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith

/**
 * Covers basic CRUD and query behaviour for [RoutineDao].
 */
@RunWith(AndroidJUnit4::class)
class RoutineDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: RoutineDao

    @BeforeTest
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        dao = db.routineDao()
        db.petDao().insert(PetEntity(id = "p1", name = "Fido"))
    }

    @AfterTest
    fun tearDown() { db.close() }

    @Test
    fun `crud operations work`() = runBlocking {
        val routine = RoutineEntity(id = "r1", petId = "p1", title = "Walk", nextDue = Instant.now())
        dao.insert(routine)
        assertEquals("Walk", dao.getById("r1")?.title)
        val updated = routine.copy(title = "Walk dog")
        dao.update(updated)
        assertEquals("Walk dog", dao.getById("r1")?.title)
        dao.delete(updated)
        assertEquals(null, dao.getById("r1"))
    }

    @Test
    fun `dueUntil returns matching routines`() = runBlocking {
        val past = Instant.now().minusSeconds(3600)
        val future = Instant.now().plusSeconds(3600)
        dao.insert(RoutineEntity(id = "r2", petId = "p1", title = "Past", nextDue = past))
        dao.insert(RoutineEntity(id = "r3", petId = "p1", title = "Future", nextDue = future))
        val due = dao.dueUntil(Instant.now()).first()
        assertEquals(1, due.size)
        assertEquals("Past", due.first().title)
    }

    @Test
    fun `foreign key cascade removes routines`() = runBlocking {
        dao.insert(RoutineEntity(id = "r4", petId = "p1", title = "Temp", nextDue = Instant.now()))
        db.petDao().delete(PetEntity(id = "p1", name = "Fido"))
        val all = dao.getAll().first()
        assertEquals(0, all.size)
    }
}
