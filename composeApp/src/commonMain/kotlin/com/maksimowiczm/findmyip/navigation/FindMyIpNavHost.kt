package com.maksimowiczm.findmyip.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.maksimowiczm.findmyip.navigation.TopRoute.Settings
import com.maksimowiczm.findmyip.ui.history.HistoryScreen
import com.maksimowiczm.findmyip.ui.home.HomeScreen
import com.maksimowiczm.findmyip.ui.motion.materialFadeThroughIn
import com.maksimowiczm.findmyip.ui.motion.materialFadeThroughOut
import com.maksimowiczm.findmyip.ui.settings.SettingsScreen
import com.maksimowiczm.findmyip.ui.settings.history.AdvancedHistorySettingsScreen
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
        navigation<Settings>(
            startDestination = Settings.SettingsHome
        ) {
            composable<Settings.SettingsHome>(
                enterTransition = { materialFadeThroughIn() },
                popEnterTransition = {
                    if (initialState.destination.hasRoute<Settings.AdvancedHistorySettings>()) {
                        ForwardBackwardComposableDefaults.popEnterTransition()
                    } else {
                        materialFadeThroughIn()
                    }
                },
                exitTransition = {
                    if (targetState.destination.hasRoute<Settings.AdvancedHistorySettings>()) {
                        ForwardBackwardComposableDefaults.exitTransition()
                    } else {
                        materialFadeThroughOut()
                    }
                },
                popExitTransition = { materialFadeThroughOut() }
            ) {
                SettingsScreen(
                    onAdvancedHistorySettingsClick = {
                        navController.navigate(
                            route = Settings.AdvancedHistorySettings,
                            navOptions = navOptions {
                                launchSingleTop = true
                            }
                        )
                    }
                )
            }
            forwardBackwardComposable<Settings.AdvancedHistorySettings>(
                enterTransition = {
                    if (initialState.destination.hasRoute<Settings.SettingsHome>()) {
                        ForwardBackwardComposableDefaults.enterTransition()
                    } else {
                        materialFadeThroughIn()
                    }
                },
                exitTransition = {
                    if (targetState.destination.hasRoute<Settings.SettingsHome>()) {
                        ForwardBackwardComposableDefaults.exitTransition()
                    } else {
                        materialFadeThroughOut()
                    }
                }
            ) {
                AdvancedHistorySettingsScreen(
                    onNavigateUp = {
                        navController.popBackStack<Settings.AdvancedHistorySettings>(
                            inclusive = true
                        )
                    }
                )
            }
        }
    }
}
