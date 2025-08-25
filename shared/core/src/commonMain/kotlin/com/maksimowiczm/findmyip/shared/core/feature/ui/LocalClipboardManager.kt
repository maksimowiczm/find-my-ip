package com.maksimowiczm.findmyip.shared.core.feature.ui

import androidx.compose.runtime.staticCompositionLocalOf
import com.maksimowiczm.findmyip.shared.core.feature.presentation.ClipboardManager

val LocalClipboardManager = staticCompositionLocalOf { ClipboardManager.noop }
