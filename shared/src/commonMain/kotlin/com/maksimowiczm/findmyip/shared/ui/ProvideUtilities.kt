package com.maksimowiczm.findmyip.shared.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.maksimowiczm.findmyip.shared.presentation.ClipboardManager
import com.maksimowiczm.findmyip.shared.presentation.DateFormatter

@Composable
fun ProvideUtilities(
    dateFormatter: DateFormatter,
    clipboardManager: ClipboardManager,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalDateFormatter provides dateFormatter,
        LocalClipboardManager provides clipboardManager,
    ) {
        content()
    }
}
