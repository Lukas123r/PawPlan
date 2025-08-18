package de.lshorizon.pawplan.ui.screen.onboarding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/** Hilt-enabled ViewModel handling onboarding state. */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
    // TODO: weitere Dependencies per @Inject hinzufügen (z.B. Repositorys)
) : ViewModel() {
    /** Mark onboarding as completed; implementation will persist flag later. */
    fun setCompleted() {
        // TODO: Persist completion flag using repository
    }
    // TODO: State/Logik
}
