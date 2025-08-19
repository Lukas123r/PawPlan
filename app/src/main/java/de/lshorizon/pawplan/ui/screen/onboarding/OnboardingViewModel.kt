// app/src/main/java/de/lshorizon/pawplan/ui/screen/onboarding/OnboardingViewModel.kt
package de.lshorizon.pawplan.ui.screen.onboarding

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.Manifest
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.lshorizon.pawplan.core.data.onboarding.OnboardingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** ViewModel managing onboarding state, navigation and permissions. */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
  private val repo: OnboardingRepository,
  private val savedStateHandle: SavedStateHandle
) : ViewModel() {
  private val lastPage = 3

  private val _uiState = MutableStateFlow(
    OnboardingUiState(
      page = 0,
      canGoBack = false,
      isLast = false,
      notificationsSupported = Build.VERSION.SDK_INT >= 33,
      notificationsGranted = false
    )
  )
  val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

  /** Advance to the next page. */
  fun next() = updatePage(_uiState.value.page + 1)

  /** Return to the previous page. */
  fun prev() = updatePage(_uiState.value.page - 1)

  /** Jump straight to the last page. */
  fun skip() = updatePage(lastPage)

  /** Persist completion flag. */
  fun finish() {
    viewModelScope.launch { repo.setCompleted(true) }
  }

  /** Store whether notifications were granted and request permission if needed. */
  fun requestNotifications(context: Context) {
    if (Build.VERSION.SDK_INT >= 33) {
      val granted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
      ) == PackageManager.PERMISSION_GRANTED
      if (!granted) {
        ActivityCompat.requestPermissions(
          context as Activity,
          arrayOf(Manifest.permission.POST_NOTIFICATIONS),
          0
        )
      }
      _uiState.update { it.copy(notificationsGranted = granted) }
    }
  }

  /** Set the current page after swipe gestures. */
  fun setPage(page: Int) = updatePage(page)

  private fun updatePage(page: Int) {
    val p = page.coerceIn(0, lastPage)
    _uiState.update {
      it.copy(
        page = p,
        canGoBack = p > 0,
        isLast = p == lastPage
      )
    }
  }
}
