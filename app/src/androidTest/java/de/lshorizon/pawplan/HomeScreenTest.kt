package de.lshorizon.pawplan

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
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

/** Simple screenshot test for the home screen demo. */
class HomeScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun launchAndTakeScreenshot() {
        composeRule.setContent {
            DemoScreen()
        }
        composeRule.onRoot().captureToImage().saveAsPng("home_screen.png")
    }
}

@Composable
private fun DemoScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .semantics { testTagsAsResourceId = true }
    ) {
        Text("Hello from UI test 👋")
    }
}

private fun ImageBitmap.saveAsPng(fileName: String) {
    val bitmap = toAndroidBitmap(this)
    val ctx = ApplicationProvider.getApplicationContext<Context>()
    val dir = File(ctx.filesDir, "screenshots").apply { mkdirs() }
    FileOutputStream(File(dir, fileName)).use { fos ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
    }
}

private fun toAndroidBitmap(img: ImageBitmap): Bitmap {
    val buffer = ByteBuffer.allocate(img.width * img.height * 4)
    img.readPixels(buffer)
    return Bitmap.createBitmap(img.width, img.height, Bitmap.Config.ARGB_8888).apply {
        buffer.rewind()
        copyPixelsFromBuffer(buffer)
    }
}

