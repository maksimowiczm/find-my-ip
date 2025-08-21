package com.maksimowiczm.findmyip.ui.infrastructure

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.maksimowiczm.findmyip.presentation.infrastructure.DateFormatter

val LocalDateFormatter = staticCompositionLocalOf { DateFormatter.noop }

@Composable
fun ProvideDateFormatter(dateFormatter: DateFormatter, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalDateFormatter provides dateFormatter, content)
}
