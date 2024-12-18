package com.maksimowiczm.findmyip.settings

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.maksimowiczm.findmyip.settings.addresshistory.AddressHistoryAdvancedSettings

@Composable
internal fun SettingsNavigation(highlight: Setting?, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = SettingsHome(highlight)) {
        composable<SettingsHome>(
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }
        ) {
            SettingsScreen(
                modifier = modifier,
                onHistorySettingsClick = navController::navigateAddressHistoryAdvancedSettings,
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
                modifier = modifier,
                onNavigateBack = { navController.popBackStack<SettingsHome>(inclusive = false) }
            )
        }
    }
}
