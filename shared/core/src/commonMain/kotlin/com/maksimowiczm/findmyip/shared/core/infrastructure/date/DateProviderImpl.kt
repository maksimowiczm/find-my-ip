package com.maksimowiczm.findmyip.shared.core.infrastructure.date

import com.maksimowiczm.findmyip.shared.core.application.infrastructure.date.DateProvider
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalTime::class)
internal class DateProviderImpl : DateProvider {
    override fun now() =
        Clock.System.now().toLocalDateTime(TimeZone.Companion.currentSystemDefault())
}
