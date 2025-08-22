package kotlinx.datetime

import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun LocalDateTime.Companion.now(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): LocalDateTime = Clock.System.now().toLocalDateTime(timeZone)

@OptIn(ExperimentalTime::class)
operator fun LocalDateTime.plus(duration: Duration): LocalDateTime =
    this.toInstant(TimeZone.UTC).plus(duration).toLocalDateTime(TimeZone.UTC)

@OptIn(ExperimentalTime::class)
operator fun LocalDateTime.minus(duration: Duration): LocalDateTime =
    this.toInstant(TimeZone.UTC).minus(duration).toLocalDateTime(TimeZone.UTC)
