package de.lshorizon.pawplan.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import de.lshorizon.pawplan.domain.routines.ListDueRoutines
import de.lshorizon.pawplan.domain.routines.RoutineDue
import de.lshorizon.pawplan.core.domain.repo.RoutineRepository
import java.time.ZoneId
import java.time.LocalDateTime
import kotlinx.coroutines.flow.first

/**
 * Hilt module wiring domain use cases to their repositories.
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {
    // Provide implementation that selects routines due before [until]
    @Provides
    @Singleton
    fun provideListDueRoutines(repo: RoutineRepository): ListDueRoutines = ListDueRoutines { until ->
        val zone = ZoneId.systemDefault()
        repo.getRoutines().first().mapNotNull { routine ->
            val last = routine.lastDone ?: LocalDateTime.MIN
            val dueTime = last.plusDays(routine.intervalDays.toLong())
            val snoozed = routine.snoozedUntil ?: dueTime
            val actualDue = if (snoozed.isAfter(dueTime)) snoozed else dueTime
            val dueInstant = actualDue.atZone(zone).toInstant()
            if (dueInstant <= until) RoutineDue(routine.id.toString(), dueInstant) else null
        }
    }
}
