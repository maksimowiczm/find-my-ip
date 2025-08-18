package com.maksimowiczm.findmyip.application.infrastructure

import kotlinx.datetime.LocalDateTime

interface DateProvider {
    fun now(): LocalDateTime
}
