package com.maksimowiczm.findmyip.data.utils

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * A simple interface to provide the current time in milliseconds.
 */
fun interface DateProvider {
    fun currentTimeMillis(): Long
}

/**
 * A default kotlinx.datetime implementation of [DateProvider] that uses the system clock.
 *
 * @see Clock.System
 */
@OptIn(ExperimentalTime::class)
val defaultKotlinDateProvider = DateProvider {
    Clock.System.now().toEpochMilliseconds()
}
