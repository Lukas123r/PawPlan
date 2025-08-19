// core/data/src/main/kotlin/de/lshorizon/pawplan/core/data/onboarding/OnboardingRepository.kt
package de.lshorizon.pawplan.core.data.onboarding

/** Repository interface persisting the onboarding completion flag. */
interface OnboardingRepository {
  /** Returns true when onboarding was finished earlier. */
  suspend fun isCompleted(): Boolean

  /** Persists whether onboarding has been finished. */
  suspend fun setCompleted(value: Boolean)
}
