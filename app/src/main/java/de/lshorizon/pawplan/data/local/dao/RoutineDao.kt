package de.lshorizon.pawplan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.lshorizon.pawplan.data.local.entity.RoutineEntity
import java.time.Instant
import kotlinx.coroutines.flow.Flow

/**
 * Handles CRUD operations for routines.
 */
@Dao
interface RoutineDao {
    @Query("SELECT * FROM routines WHERE id = :id")
    suspend fun getById(id: String): RoutineEntity?

    @Query("SELECT * FROM routines")
    fun getAll(): Flow<List<RoutineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RoutineEntity)

    @Update
    suspend fun update(entity: RoutineEntity)

    @Delete
    suspend fun delete(entity: RoutineEntity)

    @Query("SELECT * FROM routines WHERE next_due <= :until")
    fun dueUntil(until: Instant): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routines WHERE pet_id = :petId")
    fun byPet(petId: String): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routines WHERE is_active = 1")
    fun active(): Flow<List<RoutineEntity>>
}
