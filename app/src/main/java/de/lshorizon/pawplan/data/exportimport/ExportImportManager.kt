package de.lshorizon.pawplan.data.exportimport

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.room.withTransaction
import dagger.hilt.android.qualifiers.ApplicationContext
import de.lshorizon.pawplan.data.local.dao.EventLogDao
import de.lshorizon.pawplan.data.local.dao.PetDao
import de.lshorizon.pawplan.data.local.dao.RoutineDao
import de.lshorizon.pawplan.data.local.db.AppDatabase
import de.lshorizon.pawplan.data.local.entity.EventLogEntity
import de.lshorizon.pawplan.data.local.entity.PetEntity
import de.lshorizon.pawplan.data.local.entity.RoutineEntity
import kotlinx.coroutines.flow.first
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Singleton

/**
 * Offers helper functions to export and import database content as JSON files.
 */
@Singleton
class ExportImportManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val db: AppDatabase,
    private val petDao: PetDao,
    private val routineDao: RoutineDao,
    private val eventDao: EventLogDao
) {
    /**
     * Export pets, routines and events into the given SAF [uri] as a single JSON document.
     */
    suspend fun exportAllToJson(uri: Uri): Result<Unit> = runCatching {
        val resolver: ContentResolver = context.contentResolver
        resolver.openOutputStream(uri)?.use { stream ->
            val out = OutputStreamWriter(stream)
            val pets = petDao.getAll().first()
            val routines = routineDao.getAll().first()
            val events = eventDao.getAll().first()
            val json = JSONObject().apply {
                put("pets", JSONArray(pets.map { it.toJson() }))
                put("routines", JSONArray(routines.map { it.toJson() }))
                put("events", JSONArray(events.map { it.toJson() }))
            }
            out.write(json.toString())
            out.flush()
        } ?: error("Unable to open output stream")
    }

    /**
     * Import database content from the SAF [uri]. Performs validation before a transactional merge.
     */
    suspend fun importFromJson(uri: Uri): Result<ImportSummary> = runCatching {
        val resolver = context.contentResolver
        val input = resolver.openInputStream(uri) ?: error("Unable to open input stream")
        val text = BufferedReader(InputStreamReader(input)).use { it.readText() }
        val root = JSONObject(text)

        val pets = root.getJSONArray("pets").toPetEntities()
        val routines = root.getJSONArray("routines").toRoutineEntities()
        val events = root.getJSONArray("events").toEventEntities()

        db.withTransaction {
            pets.forEach { petDao.insert(it) }
            routines.forEach { routineDao.insert(it) }
            events.forEach { eventDao.insert(it) }
        }

        ImportSummary(pets.size, routines.size, events.size)
    }
}

/** Simple summary of the import process. */
data class ImportSummary(val pets: Int, val routines: Int, val events: Int)

// --- JSON helpers ---------------------------------------------------------

private fun PetEntity.toJson() = JSONObject().apply {
    put("id", id)
    put("name", name)
    birthDate?.let { put("birthDate", it.toString()) }
}

private fun RoutineEntity.toJson() = JSONObject().apply {
    put("id", id)
    put("petId", petId)
    put("title", title)
    put("nextDue", nextDue.toString())
    put("isActive", isActive)
}

private fun EventLogEntity.toJson() = JSONObject().apply {
    put("id", id)
    put("petId", petId)
    routineId?.let { put("routineId", it) }
    put("type", type)
    put("timestamp", timestamp.toString())
    metaJson?.let { put("metaJson", it) }
}

private fun JSONArray.toPetEntities(): List<PetEntity> = buildList {
    for (i in 0 until length()) {
        val obj = getJSONObject(i)
        add(
            PetEntity(
                id = obj.getString("id"),
                name = obj.getString("name"),
                birthDate = obj.optString("birthDate").takeIf { it.isNotBlank() }?.let(LocalDate::parse)
            )
        )
    }
}

private fun JSONArray.toRoutineEntities(): List<RoutineEntity> = buildList {
    for (i in 0 until length()) {
        val obj = getJSONObject(i)
        add(
            RoutineEntity(
                id = obj.getString("id"),
                petId = obj.getString("petId"),
                title = obj.getString("title"),
                nextDue = Instant.parse(obj.getString("nextDue")),
                isActive = obj.getBoolean("isActive")
            )
        )
    }
}

private fun JSONArray.toEventEntities(): List<EventLogEntity> = buildList {
    for (i in 0 until length()) {
        val obj = getJSONObject(i)
        add(
            EventLogEntity(
                id = obj.getString("id"),
                petId = obj.getString("petId"),
                routineId = obj.optString("routineId").takeIf { it.isNotBlank() },
                type = obj.getString("type"),
                timestamp = Instant.parse(obj.getString("timestamp")),
                metaJson = obj.optString("metaJson").takeIf { it.isNotBlank() }
            )
        )
    }
}
