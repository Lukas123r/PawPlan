package de.lshorizon.pawplan.data.local.db

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate

/**
 * Converts complex date types for Room.
 */
class Converters {
    @TypeConverter
    fun fromInstant(value: Long?): Instant? = value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? = instant?.toEpochMilli()

    @TypeConverter
    fun fromLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    @TypeConverter
    fun localDateToString(date: LocalDate?): String? = date?.toString()
}
