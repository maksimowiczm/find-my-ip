package com.maksimowiczm.findmyip.screenshot

import androidx.compose.runtime.Composable
import com.maksimowiczm.findmyip.shared.feature.settings.ui.SettingsScreenPreview

data object SettingsScreenScreenshot : Screenshot {
    override val name: String = "2"

    @Composable override fun Content() = SettingsScreenPreview()
}
