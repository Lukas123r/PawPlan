package de.lshorizon.pawplan.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import de.lshorizon.pawplan.data.local.dao.EventLogDao
import de.lshorizon.pawplan.data.local.dao.PetDao
import de.lshorizon.pawplan.data.local.dao.RoutineDao
import de.lshorizon.pawplan.data.local.entity.EventLogEntity
import de.lshorizon.pawplan.data.local.entity.PetEntity
import de.lshorizon.pawplan.data.local.entity.RoutineEntity

/**
 * Central Room database for the app.
 */
@Database(
    entities = [PetEntity::class, RoutineEntity::class, EventLogEntity::class],
    version = 1,
    exportSchema = true // export schema so migrations can be tracked
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun routineDao(): RoutineDao
    abstract fun eventLogDao(): EventLogDao

    companion object {
        /**
         * Placeholder migration for future schema updates.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // no-op for now
            }
        }
    }
}
