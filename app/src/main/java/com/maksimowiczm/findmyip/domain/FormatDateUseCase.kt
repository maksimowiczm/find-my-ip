package com.maksimowiczm.findmyip.domain

import java.text.DateFormat
import java.util.Date
import javax.inject.Inject

class FormatDateUseCase @Inject constructor() {
    private val defaultFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM)

    operator fun invoke(date: Date) = defaultFormat.format(date)
}
