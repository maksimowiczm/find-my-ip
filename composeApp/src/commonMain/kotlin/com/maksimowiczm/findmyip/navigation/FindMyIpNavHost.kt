package com.maksimowiczm.findmyip.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.maksimowiczm.findmyip.ui.history.HistoryScreen
import com.maksimowiczm.findmyip.ui.home.HomeScreen
import com.maksimowiczm.findmyip.ui.motion.materialFadeThroughIn
import com.maksimowiczm.findmyip.ui.motion.materialFadeThroughOut
import com.maksimowiczm.findmyip.ui.settings.SettingsScreen
import com.maksimowiczm.findmyip.ui.settings.buildPlatformSettings
import com.maksimowiczm.findmyip.ui.settings.history.AdvancedHistorySettingsScreen
import com.maksimowiczm.findmyip.ui.settings.platformSettingsGraph
import kotlinx.serialization.Serializable

sealed interface TopRoute {
    @Serializable
    data object Home : TopRoute

    @Serializable
    data object History : TopRoute

    @Serializable
    data object Settings : TopRoute {
        @Serializable
        data object SettingsHome

        @Serializable
        data object AdvancedHistorySettings
    }
}

@Composable
fun FindMyIpNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TopRoute.Home
    ) {
        composable<TopRoute.Home>(
            enterTransition = { materialFadeThroughIn() },
            exitTransition = { materialFadeThroughOut() }
        ) {
            HomeScreen()
        }
        composable<TopRoute.History>(
            enterTransition = { materialFadeThroughIn() },
            exitTransition = { materialFadeThroughOut() }
        ) {
            HistoryScreen()
        }
        navigation<TopRoute.Settings>(
            startDestination = TopRoute.Settings.SettingsHome
        ) {
            settingsComposable<TopRoute.Settings.SettingsHome> {
                SettingsScreen(
                    onAdvancedHistorySettingsClick = {
                        navController.navigate(
                            route = TopRoute.Settings.AdvancedHistorySettings,
                            navOptions = navOptions {
                                launchSingleTop = true
                            }
                        )
                    },
                    platformSettings = buildPlatformSettings(navController)
                )
            }
            settingsComposable<TopRoute.Settings.AdvancedHistorySettings> {
                AdvancedHistorySettingsScreen(
                    onNavigateUp = {
                        navController.popBackStack<TopRoute.Settings.AdvancedHistorySettings>(
                            inclusive = true
                        )
                    }
                )
            }
            platformSettingsGraph(navController)
        }
    }
}
