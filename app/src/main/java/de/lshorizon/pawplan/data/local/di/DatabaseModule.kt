package de.lshorizon.pawplan.data.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.lshorizon.pawplan.data.local.dao.EventLogDao
import de.lshorizon.pawplan.data.local.dao.PetDao
import de.lshorizon.pawplan.data.local.dao.RoutineDao
import de.lshorizon.pawplan.data.local.db.AppDatabase
import javax.inject.Singleton

/**
 * Supplies Room database and DAO instances through Hilt.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()

    @Provides
    fun providePetDao(db: AppDatabase): PetDao = db.petDao()

    @Provides
    fun provideRoutineDao(db: AppDatabase): RoutineDao = db.routineDao()

    @Provides
    fun provideEventLogDao(db: AppDatabase): EventLogDao = db.eventLogDao()
}
