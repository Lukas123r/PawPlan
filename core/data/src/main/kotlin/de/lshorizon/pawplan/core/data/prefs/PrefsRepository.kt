package de.lshorizon.pawplan.core.data.prefs

/** Repository for persisting simple preference flags like onboarding completion. */
interface PrefsRepository {
    /** Returns true when onboarding was finished earlier. */
    suspend fun hasSeenOnboarding(): Boolean

    /** Persist that the onboarding flow has been finished. */
    suspend fun setOnboardingDone(value: Boolean)
}
