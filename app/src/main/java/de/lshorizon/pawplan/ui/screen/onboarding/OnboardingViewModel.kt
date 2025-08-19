package de.lshorizon.pawplan.ui.screen.onboarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lshorizon.pawplan.core.data.prefs.PrefsRepository
import javax.inject.Inject

/** ViewModel exposing a single action to mark onboarding as finished. */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val prefs: PrefsRepository
) : ViewModel() {
    /** Persist that the user has completed the onboarding flow. */
    suspend fun markDone() = prefs.setOnboardingDone(true)
}
