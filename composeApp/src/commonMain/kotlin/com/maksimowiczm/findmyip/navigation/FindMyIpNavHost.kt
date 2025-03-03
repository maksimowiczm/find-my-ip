package com.maksimowiczm.findmyip.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.maksimowiczm.findmyip.ui.settings.language.LanguageScreen
import kotlinx.serialization.Serializable

sealed interface TopRoute {
    @Serializable
    data object Home : TopRoute

    @Serializable
    data object History : TopRoute

    @Serializable
    data object SettingsNested : TopRoute

    @Serializable
    sealed interface Settings : TopRoute {
        @Serializable
        data object SettingsHome : Settings

        @Serializable
        data object AdvancedHistorySettings : Settings

        @Serializable
        data object LanguageSettings : Settings
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
        navigation<TopRoute.SettingsNested>(
            startDestination = Settings.SettingsHome
        ) {
            settingsComposable<Settings.SettingsHome> {
                SettingsScreen(
                    onAdvancedHistorySettingsClick = {
                        navController.navigate(
                            route = Settings.AdvancedHistorySettings,
                            navOptions = navOptions {
                                launchSingleTop = true
                            }
                        )
                    },
                    onLanguageSettingsClick = {
                        navController.navigate(
                            route = Settings.LanguageSettings,
                            navOptions = navOptions {
                                launchSingleTop = true
                            }
                        )
                    }
                )
            }
            settingsComposable<Settings.AdvancedHistorySettings> {
                AdvancedHistorySettingsScreen(
                    onNavigateUp = {
                        navController.popBackStack<Settings.AdvancedHistorySettings>(
                            inclusive = true
                        )
                    }
                )
            }
            settingsComposable<Settings.LanguageSettings> {
                LanguageScreen(
                    onNavigateUp = {
                        navController.popBackStack<Settings.LanguageSettings>(
                            inclusive = true
                        )
                    }
                )
            }
        }
    }
}
