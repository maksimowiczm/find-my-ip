package com.maksimowiczm.findmyip.ui

import com.maksimowiczm.findmyip.settings.Setting
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface Route {
    @Serializable
    data object CurrentAddress : Route

    @Serializable
    data object AddressHistory : Route

    @Serializable
    data class Settings(val highlight: Setting?) : Route

    enum class Variant {
        CurrentAddress,
        AddressHistory,
        Settings
    }
}
