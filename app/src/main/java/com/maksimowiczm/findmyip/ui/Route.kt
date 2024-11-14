package com.maksimowiczm.findmyip.ui

import kotlinx.serialization.Serializable

@Serializable
data object CurrentAddressRoute

@Serializable
data object AddressHistoryRoute

@Serializable
data object SettingsRoute

enum class Route {
    CurrentAddress,
    AddressHistory,
    Settings
    ;

    companion object {
        fun fromRoute(route: String): Route {
            return when (route) {
                CurrentAddressRoute.javaClass.name -> CurrentAddress
                AddressHistoryRoute.javaClass.name -> AddressHistory
                SettingsRoute.javaClass.name -> Settings
                else -> throw IllegalArgumentException("Unknown route: $route")
            }
        }
    }
}
