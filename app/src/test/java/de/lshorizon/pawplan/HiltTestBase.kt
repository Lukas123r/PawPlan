package de.lshorizon.pawplan

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule

/**
 * Base class setting up Hilt injection for unit tests.
 * Keeps test classes concise and prevents missing inject calls.
 */
@HiltAndroidTest
open class HiltTestBase {
    @get:Rule
    val hilt = HiltAndroidRule(this)

    @Before
    fun init() {
        hilt.inject()
    }
}
