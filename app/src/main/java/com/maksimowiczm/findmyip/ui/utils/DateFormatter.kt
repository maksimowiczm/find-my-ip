package com.maksimowiczm.findmyip.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface DateFormatter {
    fun formatDateTime(dateTime: LocalDateTime): String
    fun formatDateShort(dateTime: LocalDate): String
}

private val defaultDateFormatter: DateFormatter = object : DateFormatter {
    override fun formatDateTime(dateTime: LocalDateTime) = dateTime.toString()
    override fun formatDateShort(dateTime: LocalDate) = dateTime.toString()
}

val LocalDateFormatter = staticCompositionLocalOf { defaultDateFormatter }

@Composable
fun DateFormatterProvider(dateFormatter: DateFormatter, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalDateFormatter provides dateFormatter
    ) {
        content()
    }
}
