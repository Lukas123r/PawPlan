package de.lshorizon.pawplan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.lshorizon.pawplan.data.local.entity.EventLogEntity
import kotlinx.coroutines.flow.Flow

/**
 * Exposes operations for reading and writing event logs.
 */
@Dao
interface EventLogDao {
    @Query("SELECT * FROM event_logs WHERE id = :id")
    suspend fun getById(id: String): EventLogEntity?

    @Query("SELECT * FROM event_logs")
    fun getAll(): Flow<List<EventLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: EventLogEntity)

    @Update
    suspend fun update(entity: EventLogEntity)

    @Delete
    suspend fun delete(entity: EventLogEntity)

    @Query("SELECT * FROM event_logs WHERE pet_id = :petId")
    fun byPet(petId: String): Flow<List<EventLogEntity>>

    @Query("SELECT * FROM event_logs WHERE routine_id = :routineId")
    fun byRoutine(routineId: String): Flow<List<EventLogEntity>>
}
