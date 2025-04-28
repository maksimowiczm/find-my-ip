package com.maksimowiczm.findmyip.infrastructure.android

import android.content.Context
import android.text.format.DateFormat
import com.maksimowiczm.findmyip.ui.utils.DateFormatter
import java.time.format.DateTimeFormatter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime

class AndroidDateFormatter(private val context: Context) : DateFormatter {
    override fun formatDateTime(dateTime: LocalDateTime): String =
        if (DateFormat.is24HourFormat(context)) {
            DateTimeFormatter
                .ofPattern("d MMMM yyyy HH:mm", context.defaultLocale)
                .format(dateTime.toJavaLocalDateTime())
        } else {
            DateTimeFormatter
                .ofPattern("d MMMM yyyy hh:mm a", context.defaultLocale)
                .format(dateTime.toJavaLocalDateTime())
        }
}
