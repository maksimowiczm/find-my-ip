package com.maksimowiczm.findmyip.feature.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale

@Composable
internal actual fun currentLanguage(): String = remember {
    Locale.current.platformLocale.displayName
}
