package com.maksimowiczm.findmyip.data

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.datetime.LocalDateTime

actual class StringFormatRepository {
    /**
     * Formats the specified [date] as a string in the "d MMMM yyyy hh:mm:ss a" format. In 12-hour
     * format with AM/PM.
     */
    actual fun formatDateTime(date: LocalDateTime): String {
        val javaDate = Calendar.getInstance().apply {
            set(
                date.year,
                date.monthNumber - 1,
                date.dayOfMonth,
                date.hour,
                date.minute,
                date.second
            )
        }.time

        val dateFormat = SimpleDateFormat("d MMMM yyyy hh:mm:ss a", Locale.getDefault())

        return dateFormat.format(javaDate)
    }
}
