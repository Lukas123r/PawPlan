package de.lshorizon.pawplan.ui.screen.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Simple onboarding screen with a few intro slides and a final action.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(), // ViewModel provided by Hilt
    onFinished: () -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { 3 })

    Column(Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            when (page) {
                // Intro slide showing app purpose
                0 -> OnboardingSlide(Icons.Outlined.Pets, "Welcome", "Manage all your pets in one place.")
                1 -> OnboardingSlide(Icons.Default.List, "Routines", "Organize daily care tasks with ease.")
                2 -> OnboardingSlide(Icons.Default.Check, "Ready?", "Let's add your first friend!")
            }
        }
        if (pagerState.currentPage == 2) {
            Button(
                onClick = {
                    viewModel.setCompleted()
                    onFinished()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) { Text("Get started") }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    viewModel.setCompleted()
                    onFinished()
                }) { Text("Skip") }
            }
        }
    }
}

/**
 * Represents a single slide inside the onboarding pager.
 */
@Composable
private fun OnboardingSlide(icon: ImageVector, title: String, text: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(120.dp))
        Spacer(Modifier.height(24.dp))
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
    }
}

