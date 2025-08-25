package com.maksimowiczm.findmyip.shared.core.presentation

import android.content.Context
import android.text.format.DateFormat
import com.maksimowiczm.findmyip.shared.core.feature.presentation.DateFormatter
import com.maksimowiczm.findmyip.shared.core.infrastructure.defaultLocale
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime

class AndroidDateFormatter(private val context: Context) : DateFormatter {
    private val defaultLocale: Locale
        get() = context.defaultLocale

    override fun formatDateTimeLong(dateTime: LocalDateTime): String {
        val pattern =
            if (DateFormat.is24HourFormat(context)) {
                "d MMMM yyyy, HH:mm:ss"
            } else {
                "d MMMM yyyy, hh:mm:ss a"
            }

        return DateTimeFormatter.ofPattern(pattern, defaultLocale)
            .format(dateTime.toJavaLocalDateTime())
    }
}
