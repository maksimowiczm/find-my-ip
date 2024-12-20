package com.maksimowiczm.findmyip.settings

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.maksimowiczm.findmyip.settings.addresshistory.AddressHistoryAdvancedSettings
import kotlinx.serialization.Serializable

@Serializable
internal data object SettingsRoute

@Serializable
internal data class SettingsHome(val highlight: Setting?)

@Serializable
internal enum class Setting {
    SaveHistory,
    ClearHistory,
    InternetProtocolVersion
}

@Serializable
internal data object AddressHistoryAdvancedSettings

internal fun NavGraphBuilder.settingsGraph(
    onAddressHistoryAdvancedSettings: () -> Unit,
    onAddressHistoryAdvancedSettingsNavigateBack: () -> Unit
) {
    navigation<SettingsRoute>(
        startDestination = SettingsHome(null)
    ) {
        composable<SettingsHome>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }
        ) {
            SettingsScreen(
                modifier = Modifier.fillMaxSize(),
                onHistorySettingsClick = onAddressHistoryAdvancedSettings,
                highlightSetting = it.toRoute<SettingsHome>().highlight
            )
        }
        composable<AddressHistoryAdvancedSettings>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }
        ) {
            AddressHistoryAdvancedSettings(
                modifier = Modifier.fillMaxSize(),
                onNavigateBack = onAddressHistoryAdvancedSettingsNavigateBack
            )
        }
    }
}

internal fun NavController.navigateSettingsHome(
    highlight: Setting? = null,
    navOptions: NavOptions? = null
) {
    navigate(SettingsHome(highlight), navOptions)
}

internal fun NavController.navigateAddressHistoryAdvancedSettings(navOptions: NavOptions? = null) {
    navigate(AddressHistoryAdvancedSettings, navOptions)
}
