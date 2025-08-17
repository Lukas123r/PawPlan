package de.lshorizon.pawplan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.lshorizon.pawplan.data.local.entity.PetEntity
import kotlinx.coroutines.flow.Flow

/**
 * Provides data access helpers for pets.
 */
@Dao
interface PetDao {
    @Query("SELECT * FROM pets WHERE id = :id")
    suspend fun getById(id: String): PetEntity?

    @Query("SELECT * FROM pets")
    fun getAll(): Flow<List<PetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PetEntity)

    @Update
    suspend fun update(entity: PetEntity)

    @Delete
    suspend fun delete(entity: PetEntity)
}
