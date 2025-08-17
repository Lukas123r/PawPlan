package de.lshorizon.pawplan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.UUID

/**
 * Represents a single pet stored in the local database.
 */
@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val birthDate: LocalDate? = null
)
