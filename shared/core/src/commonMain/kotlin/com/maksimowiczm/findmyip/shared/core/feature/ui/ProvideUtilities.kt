package com.maksimowiczm.findmyip.shared.core.feature.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.maksimowiczm.findmyip.shared.core.feature.presentation.ClipboardManager
import com.maksimowiczm.findmyip.shared.core.feature.presentation.DateFormatter

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
