package com.maksimowiczm.findmyip.screenshot

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onRoot
import com.maksimowiczm.findmyip.ui.infrastructure.AndroidDateFormatter
import com.maksimowiczm.findmyip.ui.infrastructure.LocalDateFormatter

@OptIn(ExperimentalTestApi::class)
actual suspend fun ComposeUiTest.capture(screenshot: Screenshot) {
    var context: Context? = null

    setContent {
        context = LocalContext.current
        CompositionLocalProvider(LocalDateFormatter provides AndroidDateFormatter(context)) {
            screenshot.Content()
        }
    }

    awaitIdle()

    val bitmap = onRoot().captureToImage().asAndroidBitmap()
    saveScreenshotToPictures(context!!, bitmap, screenshot.name)
}

private fun saveScreenshotToPictures(context: Context, bitmap: Bitmap, name: String) {
    val values =
        ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$name.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/${context.packageName}",
            )
        }

    val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    uri?.let {
        context.contentResolver.openOutputStream(it)?.use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
    }
}
