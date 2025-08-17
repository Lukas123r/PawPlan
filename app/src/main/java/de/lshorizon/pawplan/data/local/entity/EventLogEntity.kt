package de.lshorizon.pawplan.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

/**
 * Stores an event that happened to a pet, optionally linked to a routine.
 */
@Entity(
    tableName = "event_logs",
    foreignKeys = [
        ForeignKey(
            entity = PetEntity::class,
            parentColumns = ["id"],
            childColumns = ["pet_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["routine_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("pet_id"), Index("routine_id")]
)
data class EventLogEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "pet_id") val petId: String,
    @ColumnInfo(name = "routine_id") val routineId: String?,
    val type: String,
    val timestamp: Instant,
    @ColumnInfo(name = "meta_json") val metaJson: String? = null
)
