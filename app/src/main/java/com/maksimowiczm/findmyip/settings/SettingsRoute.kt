package com.maksimowiczm.findmyip.settings

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
internal data class SettingsHome(
    val highlight: Setting?
)

@Serializable
internal data object AddressHistoryAdvancedSettings

internal fun NavController.navigateAddressHistoryAdvancedSettings(navOptions: NavOptions? = null) {
    navigate(AddressHistoryAdvancedSettings, navOptions)
}
