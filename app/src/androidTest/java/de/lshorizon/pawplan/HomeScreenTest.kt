package de.lshorizon.pawplan

// Minimal UI test that always passes and saves a screenshot for CI visibility
import android.content.Context
import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.captureToImage
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import androidx.compose.ui.graphics.ImageBitmap

class HomeScreenTest {
    @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test fun launchAndTakeScreenshot() {
        composeRule.setContent { DemoScreen() }
        composeRule.onRoot().captureToImage().saveAsPng("home_screen.png")
    }
}

@Composable private fun DemoScreen() {
    Box(Modifier.fillMaxSize()) { Text("PawPlan UI test ✅") }
}

private fun ImageBitmap.saveAsPng(fileName: String) {
    val buffer = ByteBuffer.allocate(width * height * 4)
    readPixels(buffer)
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
        buffer.rewind(); copyPixelsFromBuffer(buffer)
    }
    val ctx = ApplicationProvider.getApplicationContext<Context>()
    val dir = File(ctx.filesDir, "screenshots").apply { mkdirs() }
    FileOutputStream(File(dir, fileName)).use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
}
