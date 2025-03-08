package com.maksimowiczm.findmyip.data

import kotlinx.datetime.LocalDateTime

actual class StringFormatRepository {
    /**
     * Formats the specified [date] as a string in the "d MMMM yyyy hh:mm:ss" format. In 12-hour
     * format with AM/PM.
     */
    actual fun formatDateTime(date: LocalDateTime): String {
        TODO("Not yet implemented")
    }
}