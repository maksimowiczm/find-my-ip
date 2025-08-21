package com.maksimowiczm.findmyip.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsRoute(
    onBack: () -> Unit,
    onContribute: () -> Unit,
    onLanguage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsScreen(
        onBack = onBack,
        onContribute = onContribute,
        onLanguage = onLanguage,
        modifier = modifier,
    )
}
