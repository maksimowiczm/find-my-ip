package com.maksimowiczm.findmyip.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.maksimowiczm.findmyip.ui.motion.materialFadeThroughIn
import com.maksimowiczm.findmyip.ui.motion.materialFadeThroughOut
import com.maksimowiczm.findmyip.ui.page.history.HistoryPage
import com.maksimowiczm.findmyip.ui.page.home.HomePage
import com.maksimowiczm.findmyip.ui.page.settings.SettingsPage
import com.maksimowiczm.findmyip.ui.page.settings.language.LanguagePage
import com.maksimowiczm.findmyip.ui.page.settings.notifications.NotificationsPage

@Composable
fun FindMyIpNavHost(appNavigationState: AppNavigationState, modifier: Modifier = Modifier) {
    val navController = appNavigationState.navController

    NavHost(
        navController = appNavigationState.navController,
        startDestination = Home,
        modifier = modifier
    ) {
        composable<Home>(
            enterTransition = { materialFadeThroughIn() },
            exitTransition = { materialFadeThroughOut() }
        ) {
            HomePage()
        }
        composable<History>(
            enterTransition = { materialFadeThroughIn() },
            exitTransition = { materialFadeThroughOut() }
        ) {
            HistoryPage()
        }
        navigation<Settings>(
            startDestination = SettingsHome
        ) {
            settingsComposable<SettingsHome> {
                SettingsPage(
                    onBackgroundServices = {
                        // TODO
                    },
                    onNotifications = {
                        navController.navigate(NotificationsSettings) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onLanguage = {
                        navController.navigate(LanguageSettings) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            settingsComposable<NotificationsSettings> {
                NotificationsPage(
                    onBack = {
                        navController.popBackStack<NotificationsSettings>(inclusive = true)
                    }
                )
            }
            settingsComposable<LanguageSettings> {
                LanguagePage(
                    onBack = {
                        navController.popBackStack<LanguageSettings>(inclusive = true)
                    }
                )
            }
        }
    }
}
