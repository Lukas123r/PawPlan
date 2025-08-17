package de.lshorizon.pawplan.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

/**
 * Defines a routine that belongs to a pet and is scheduled locally.
 */
@Entity(
    tableName = "routines",
    foreignKeys = [
        ForeignKey(
            entity = PetEntity::class,
            parentColumns = ["id"],
            childColumns = ["pet_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("pet_id")]
)
data class RoutineEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "pet_id") val petId: String,
    val title: String,
    @ColumnInfo(name = "next_due") val nextDue: Instant,
    @ColumnInfo(name = "is_active") val isActive: Boolean = true
)
