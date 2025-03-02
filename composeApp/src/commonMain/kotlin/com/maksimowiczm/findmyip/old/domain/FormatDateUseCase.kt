package com.maksimowiczm.findmyip.old.domain

import java.text.DateFormat
import java.util.Date

class FormatDateUseCase {
    private val defaultFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM)

    operator fun invoke(date: Date): String = defaultFormat.format(date)
}
