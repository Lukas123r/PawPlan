// app/src/main/java/de/lshorizon/pawplan/ui/screen/onboarding/OnboardingUiState.kt
package de.lshorizon.pawplan.ui.screen.onboarding

/** Represents the UI state for the onboarding flow. */
data class OnboardingUiState(
  val page: Int = 0,
  val canGoBack: Boolean = false,
  val isLast: Boolean = false,
  val notificationsSupported: Boolean = false,
  val notificationsGranted: Boolean = false
)

/** Events emitted from the onboarding UI. */
sealed interface OnboardingEvent {
  object Next : OnboardingEvent
  object Prev : OnboardingEvent
  object Skip : OnboardingEvent
  object Finish : OnboardingEvent
}
