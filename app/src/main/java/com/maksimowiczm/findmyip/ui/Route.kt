package com.maksimowiczm.findmyip.ui

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object CurrentAddress : Route

    @Serializable
    data object AddressHistory : Route

    @Serializable
    data object Settings : Route

    enum class Variant {
        CurrentAddress,
        AddressHistory,
        Settings
    }
}
