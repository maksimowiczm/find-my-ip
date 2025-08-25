package com.maksimowiczm.findmyip.shared.feature.settings.ui

import androidx.compose.runtime.Composable
import com.maksimowiczm.findmyip.shared.core.feature.ui.FindMyIpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun SettingsScreenPreview() {
    FindMyIpTheme {
        SettingsScreen(onBack = {}, onContribute = {}, onRunInBackground = {}, onLanguage = {})
    }
}
