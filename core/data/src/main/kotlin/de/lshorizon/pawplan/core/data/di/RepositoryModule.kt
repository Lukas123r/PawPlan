// core/data/src/main/kotlin/de/lshorizon/pawplan/core/data/di/RepositoryModule.kt
package de.lshorizon.pawplan.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.lshorizon.pawplan.core.domain.repo.RoutineRepository
import de.lshorizon.pawplan.core.data.repo.RoutineRepositoryImpl
import de.lshorizon.pawplan.core.data.onboarding.OnboardingRepository
import de.lshorizon.pawplan.core.data.onboarding.OnboardingRepositoryImpl
import javax.inject.Singleton

// Hilt module that provides repository implementations
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
  @Binds
  @Singleton
  abstract fun bindRoutineRepository(
    impl: RoutineRepositoryImpl
  ): RoutineRepository

  @Binds
  @Singleton
  abstract fun bindOnboardingRepository(
    impl: OnboardingRepositoryImpl
  ): OnboardingRepository
}
