package com.maksimowiczm.whatismyip.domain

import java.text.DateFormat
import java.util.Date

class FormatDateUseCase(private val defaultFormatter: DateFormat) {
    operator fun invoke(date: Date) = defaultFormatter.format(date)
}
