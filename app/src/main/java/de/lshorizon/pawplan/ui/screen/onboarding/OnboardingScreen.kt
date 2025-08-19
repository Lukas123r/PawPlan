package de.lshorizon.pawplan.ui.screen.onboarding

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import de.lshorizon.pawplan.R
import de.lshorizon.pawplan.core.navigation.NavRoutes
import kotlinx.coroutines.launch

/** Full onboarding flow using a pager and persisted completion flag. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    vm: OnboardingViewModel = hiltViewModel()
) {
    // Static list of pages shown in the onboarding carousel
    val pages = listOf(
        OnbPage(R.drawable.ic_onb_welcome, R.string.onboarding_title_1, R.string.onboarding_body_1),
        OnbPage(R.drawable.ic_onb_notifs, R.string.onboarding_title_2, R.string.onboarding_body_2),
        OnbPage(R.drawable.ic_onb_templates, R.string.onboarding_title_3, R.string.onboarding_body_3),
        OnbPage(R.drawable.ic_onb_sync, R.string.onboarding_title_4, R.string.onboarding_body_4),
    )

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pages.size })
    val isLast = pagerState.currentPage == pages.lastIndex
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Top bar hosting the optional skip action and handling status bar insets
            TopAppBar(
                title = {},
                actions = {
                    if (!isLast) {
                        TextButton(onClick = {
                            scope.launch { vm.markDone() }
                            navController.navigate(NavRoutes.Home.route) {
                                popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                            }
                        }) { Text(stringResource(R.string.onboarding_skip)) }
                    }
                },
                windowInsets = androidx.compose.foundation.layout.WindowInsets.statusBars,
            )
        },
        bottomBar = {
            Column(
                Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                DotsIndicator(
                    totalDots = pages.size,
                    selectedIndex = pagerState.currentPage,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(12.dp))
                val label = if (isLast) R.string.onboarding_done else R.string.onboarding_next
                Button(
                    onClick = {
                        if (isLast) {
                            scope.launch { vm.markDone() }
                            navController.navigate(NavRoutes.Home.route) {
                                popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                            }
                        } else {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) { Text(stringResource(label)) }
            }
        }
    ) { inner ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) { index ->
            val p = pages[index]
            OnbPageContent(
                image = p.image,
                title = stringResource(p.title),
                body = stringResource(p.body)
            )
        }
    }
}

/** Simple model describing the content of a single onboarding page. */
data class OnbPage(@DrawableRes val image: Int, @StringRes val title: Int, @StringRes val body: Int)

/** Layout of a single onboarding page with image, title and body text. */
@Composable
private fun OnbPageContent(
    @DrawableRes image: Int,
    title: String,
    body: String
) {
    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(24.dp))
            )
            Spacer(Modifier.height(24.dp))
            Text(title, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(
                body,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** Dots indicator reflecting the currently visible onboarding page. */
@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(totalDots) { i ->
            val active = i == selectedIndex
            Box(
                Modifier
                    .size(if (active) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
            )
        }
    }
}

