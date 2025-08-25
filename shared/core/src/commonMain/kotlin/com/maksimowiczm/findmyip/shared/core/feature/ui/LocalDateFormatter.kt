package com.maksimowiczm.findmyip.shared.core.feature.ui

import androidx.compose.runtime.staticCompositionLocalOf
import com.maksimowiczm.findmyip.shared.core.feature.presentation.DateFormatter

val LocalDateFormatter = staticCompositionLocalOf { DateFormatter.noop }
