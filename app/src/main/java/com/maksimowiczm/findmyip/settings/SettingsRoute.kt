package com.maksimowiczm.findmyip.settings

import kotlinx.serialization.Serializable

@Serializable
internal data class SettingsHome(
    val highlight: Setting?
)

@Serializable
internal data object AddressHistoryAdvancedSettings
