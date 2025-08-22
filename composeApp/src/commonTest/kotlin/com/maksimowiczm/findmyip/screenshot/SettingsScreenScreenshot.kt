package com.maksimowiczm.findmyip.screenshot

import androidx.compose.runtime.Composable
import com.maksimowiczm.findmyip.ui.settings.SettingsScreen

data object SettingsScreenScreenshot : Screenshot {
    override val name: String = "2"

    @Composable
    override fun Content() {
        SettingsScreen(onBack = {}, onContribute = {}, onLanguage = {})
    }
}
