package com.maksimowiczm.findmyip.shared.feature.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsRoute(
    onBack: () -> Unit,
    onContribute: () -> Unit,
    onRunInBackground: () -> Unit,
    onLanguage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsScreen(
        onBack = onBack,
        onContribute = onContribute,
        onRunInBackground = onRunInBackground,
        onLanguage = onLanguage,
        modifier = modifier,
    )
}
