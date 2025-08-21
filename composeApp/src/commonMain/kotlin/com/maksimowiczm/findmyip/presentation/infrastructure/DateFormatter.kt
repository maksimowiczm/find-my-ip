package com.maksimowiczm.findmyip.presentation.infrastructure

import kotlinx.datetime.LocalDateTime

interface DateFormatter {
    /**
     * Formats the specified [dateTime] as a string in the "d MMMM yyyy, hh:mm:ss" format.
     *
     * For example, in English (US), this could return "21 April 2025, 14:30:23".
     */
    fun formatDateTimeLong(dateTime: LocalDateTime): String

    companion object {
        val noop: DateFormatter =
            object : DateFormatter {
                override fun formatDateTimeLong(dateTime: LocalDateTime): String =
                    dateTime.toString()
            }
    }
}
