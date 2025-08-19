// app/src/main/java/de/lshorizon/pawplan/ui/screen/onboarding/OnboardingScreen.kt
package de.lshorizon.pawplan.ui.screen.onboarding

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.lshorizon.pawplan.R
import de.lshorizon.pawplan.core.design.PawPlanTheme

/**
 * Representation of a single static onboarding page using only resource IDs.
 */
data class OnboardingPage(
  @DrawableRes val imageRes: Int,
  @StringRes val titleRes: Int,
  @StringRes val subtitleRes: Int
)

// List of static pages resolved later inside composables
private val ONBOARDING_PAGES = listOf(
  OnboardingPage(
    R.drawable.ic_onboarding_placeholder,
    R.string.onboarding_welcome_title,
    R.string.onboarding_welcome_text
  ),
  OnboardingPage(
    R.drawable.ic_onboarding_placeholder,
    R.string.onboarding_feature_notifications,
    R.string.onboarding_feature_templates
  )
)

/** Onboarding UI with pager and navigation actions. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
  onFinished: () -> Unit,
  vm: OnboardingViewModel = hiltViewModel()
) {
  val state by vm.uiState.collectAsState()
  // Count static pages plus dynamic permission and privacy pages
  val pages = ONBOARDING_PAGES.size + 2
  val pagerState = rememberPagerState(initialPage = state.page, pageCount = { pages })
  LaunchedEffect(state.page) { pagerState.animateScrollToPage(state.page) }
  LaunchedEffect(pagerState.currentPage) { vm.setPage(pagerState.currentPage) }

  val context = LocalContext.current
  // Static start button text after replacing invalid "{str}" placeholder
  val startLabel = stringResource(R.string.onboarding_start_label)
  val backCd = stringResource(R.string.onboarding_back_cd)
  val skipCd = stringResource(R.string.onboarding_skip_cd)
  val finishCd = stringResource(R.string.onboarding_finish_cd, startLabel)
  val nextCd = stringResource(R.string.onboarding_next_cd)
  val buttonCd = if (state.isLast) finishCd else nextCd

  Column(modifier = Modifier.fillMaxSize()) {
    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
      if (state.canGoBack) {
        IconButton(
          onClick = { vm.prev() },
          modifier = Modifier.align(Alignment.CenterStart).semantics {
            // Describe back navigation for accessibility
            contentDescription = backCd
          }
        ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null) }
      }
      if (!state.isLast) {
        TextButton(
          onClick = { vm.skip() },
          modifier = Modifier.align(Alignment.CenterEnd).semantics {
            // Describe skipping action for accessibility
            contentDescription = skipCd
          }
        ) { Text(stringResource(R.string.onboarding_skip)) }
      }
    }

    HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
      when (page) {
        in ONBOARDING_PAGES.indices -> OnboardingPageContent(ONBOARDING_PAGES[page])
        ONBOARDING_PAGES.size -> PageNotifications(
          supported = state.notificationsSupported,
          granted = state.notificationsGranted,
          onRequest = { vm.requestNotifications(context) }
        )
        else -> PagePrivacy(
          onOpenSettings = {
            val intent = Intent(
              Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
              Uri.fromParts("package", context.packageName, null)
            )
            context.startActivity(intent)
          }
        )
      }
    }

    DotsIndicator(total = pages, selected = state.page)

    Button(
      onClick = {
        if (state.isLast) {
          vm.finish()
          onFinished()
        } else {
          vm.next()
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .semantics {
          // Announce the button purpose based on current page
          contentDescription = buttonCd
        }
    ) {
      Text(
        if (state.isLast) {
          // Show formatted text when finishing onboarding
          stringResource(R.string.onboarding_start, startLabel)
        } else {
          stringResource(R.string.onboarding_next)
        }
      )
    }
  }
}

/** Displays a static onboarding page based on resource IDs. */
@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
  val image = painterResource(page.imageRes)
  val title = stringResource(page.titleRes)
  val subtitle = stringResource(page.subtitleRes)
  Column(
    modifier = Modifier.fillMaxSize().padding(32.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Image(painter = image, contentDescription = null, modifier = Modifier.size(120.dp))
    Spacer(Modifier.size(24.dp))
    Text(title, style = MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.size(8.dp))
    Text(subtitle, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
  }
}

/** Page asking for notification permission. */
@Composable
private fun PageNotifications(
  supported: Boolean,
  granted: Boolean,
  onRequest: () -> Unit
) {
  Column(
    modifier = Modifier.fillMaxSize().padding(32.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(Icons.Filled.Notifications, contentDescription = null, modifier = Modifier.size(120.dp))
    Spacer(Modifier.size(24.dp))
    Text(stringResource(R.string.onboarding_notifications_title), style = MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.size(8.dp))
    Text(stringResource(R.string.onboarding_notifications_text), style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
    if (supported && !granted) {
      Spacer(Modifier.size(24.dp))
      Button(onClick = onRequest) {
        Text(stringResource(R.string.onboarding_allow_notifications))
      }
    }
  }
}

/** Simple privacy info page with link to settings. */
@Composable
private fun PagePrivacy(onOpenSettings: () -> Unit) {
  Column(
    modifier = Modifier.fillMaxSize().padding(32.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(120.dp))
    Spacer(Modifier.size(24.dp))
    Text(stringResource(R.string.onboarding_privacy_title), style = MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.size(8.dp))
    Text(stringResource(R.string.onboarding_privacy_text), style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
    Spacer(Modifier.size(24.dp))
    TextButton(onClick = onOpenSettings) { Text(stringResource(R.string.onboarding_privacy_settings)) }
  }
}

/** Row of dots indicating pager progress. */
@Composable
private fun DotsIndicator(total: Int, selected: Int) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center
  ) {
    repeat(total) { index ->
      val size = if (index == selected) 12.dp else 8.dp
      val color = if (index == selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
      Box(
        Modifier
          .padding(4.dp)
          .size(size)
          .background(color, CircleShape)
      )
    }
  }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview
@androidx.compose.ui.tooling.preview.Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
private fun PreviewOnboarding() {
  PawPlanTheme { OnboardingScreen(onFinished = {}) }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview
@androidx.compose.ui.tooling.preview.Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
private fun PreviewPages() {
  PawPlanTheme {
    Column {
      OnboardingPageContent(ONBOARDING_PAGES.first())
      PageNotifications(true, false) {}
      PagePrivacy {}
    }
  }
}
