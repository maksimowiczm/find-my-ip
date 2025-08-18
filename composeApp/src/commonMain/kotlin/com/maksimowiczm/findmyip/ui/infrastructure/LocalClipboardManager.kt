package com.maksimowiczm.findmyip.ui.infrastructure

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.maksimowiczm.findmyip.presentation.infrastructure.ClipboardManager

val LocalClipboardManager = staticCompositionLocalOf { ClipboardManager.noop }

@Composable
fun ProvideClipboardManager(clipboardManager: ClipboardManager, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalClipboardManager provides clipboardManager, content)
}
