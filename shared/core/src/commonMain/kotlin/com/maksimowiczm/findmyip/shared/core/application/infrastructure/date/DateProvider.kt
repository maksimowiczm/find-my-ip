package com.maksimowiczm.findmyip.shared.core.application.infrastructure.date

import kotlinx.datetime.LocalDateTime

interface DateProvider {
    fun now(): LocalDateTime
}
