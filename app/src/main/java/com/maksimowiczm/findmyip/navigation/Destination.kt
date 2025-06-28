package com.maksimowiczm.findmyip.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination

@Serializable
sealed interface TopDestination : Destination

@Serializable
data object Home : TopDestination

@Serializable
data object History : TopDestination

@Serializable
data object Settings : TopDestination

@Serializable
data object SettingsHome : Destination

@Serializable
data object BackgroundServicesSettings : Destination

@Serializable
data object NotificationsSettings : Destination

@Serializable
data object LanguageSettings : Destination

@Serializable
data object Sponsor : Destination

@Serializable
data object About : Destination
