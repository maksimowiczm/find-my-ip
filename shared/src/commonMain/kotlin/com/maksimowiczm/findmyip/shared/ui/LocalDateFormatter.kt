package com.maksimowiczm.findmyip.shared.ui

import androidx.compose.runtime.staticCompositionLocalOf
import com.maksimowiczm.findmyip.shared.presentation.DateFormatter

val LocalDateFormatter = staticCompositionLocalOf { DateFormatter.noop }
