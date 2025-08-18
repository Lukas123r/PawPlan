package de.lshorizon.pawplan.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import de.lshorizon.pawplan.MainActivity
import org.junit.Rule
import org.junit.Test

/**
 * Basic instrumentation test navigating bottom navigation items.
 */
class SmokeTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `app launches and navigates`() {
        composeRule.onNodeWithText("Pets").performClick()
        composeRule.onNodeWithText("No pets yet").assertIsDisplayed()
        composeRule.onNodeWithText("Settings").performClick()
        composeRule.onNodeWithText("Settings").assertIsDisplayed()
        composeRule.onNodeWithText("Home").performClick()
        composeRule.onNodeWithText("Due Now").assertIsDisplayed()
    }
}
