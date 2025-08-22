package de.lshorizon.pawplan

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * CI Smoke Test:
 * Verifies that the test framework runs in CI.
 * This is a real JUnit test under the app's actual package namespace.
 */
class CiSmokeTest {
    @Test
    fun ciIsAbleToRunUnitTests() {
        assertTrue("CI can execute JVM unit tests", true)
    }
}
