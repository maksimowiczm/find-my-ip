package com.maksimowiczm.findmyip.data

import android.content.Context
import java.time.format.DateTimeFormatter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime

actual class StringFormatRepository(private val context: Context) {
    /**
     * Formats the specified [date] as a string in the "d MMMM yyyy, HH:mm:ss" format. In 12-hour
     * format with AM/PM.
     */
    actual fun formatDateTime(date: LocalDateTime): String {
        val is24HourFormat = android.text.format.DateFormat.is24HourFormat(context)

        val format = if (is24HourFormat) {
            "d MMMM yyyy, HH:mm:ss"
        } else {
            "d MMMM yyyy, hh:mm:ss a"
        }

        val formatter = DateTimeFormatter.ofPattern(format)

        return formatter.format(date.toJavaLocalDateTime())
    }
}
