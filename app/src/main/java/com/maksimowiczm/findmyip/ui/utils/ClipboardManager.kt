package com.maksimowiczm.findmyip.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

interface ClipboardManager {
    fun copyText(label: String, text: String)
}

private val defaultClipboardManager: ClipboardManager = object : ClipboardManager {
    override fun copyText(label: String, text: String) {
        // Default implementation does nothing
    }
}

val LocalClipboardManager = staticCompositionLocalOf { defaultClipboardManager }

@Composable
fun ClipboardManagerProvider(clipboardManager: ClipboardManager, content: @Composable () -> Unit) {
    androidx.compose.runtime.CompositionLocalProvider(
        LocalClipboardManager provides clipboardManager
    ) {
        content()
    }
}
