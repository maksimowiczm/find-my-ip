package com.maksimowiczm.findmyip.data

import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime

actual class StringFormatRepository {
    /**
     * Formats the specified [date] as a string in the "d MMMM yyyy hh:mm:ss a" format. In 12-hour
     * format with AM/PM.
     */
    actual fun formatDateTime(date: LocalDateTime): String {
        val format = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault())

        return format.format(date.toJavaLocalDateTime())
    }
}
