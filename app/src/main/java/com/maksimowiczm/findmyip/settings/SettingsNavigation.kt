package com.maksimowiczm.findmyip.settings

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maksimowiczm.findmyip.settings.addresshistory.HistoryAdvancedSettings

@Composable
fun SettingsNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = SettingsHome) {
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
                onHistorySettingsClick = { navController.navigate(AddressHistoryAdvancedSettings) }
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
            HistoryAdvancedSettings(
                modifier = modifier,
                onNavigateBack = { navController.popBackStack(SettingsHome, false) }
            )
        }
    }
}
