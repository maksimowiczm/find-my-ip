package com.maksimowiczm.findmyip.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale

@Composable
actual fun currentLanguage(): String = remember { Locale.current.platformLocale.displayName }
