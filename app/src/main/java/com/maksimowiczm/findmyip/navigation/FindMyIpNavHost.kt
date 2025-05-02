package com.maksimowiczm.findmyip.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.ui.page.home.HomePage
import com.maksimowiczm.findmyip.ui.page.settings.SettingsPage
import com.maksimowiczm.findmyip.ui.page.settings.language.LanguagePage
import com.maksimowiczm.findmyip.ui.page.settings.notifications.NotificationsPage

@Composable
fun FindMyIpNavHost(
    modifier: Modifier = Modifier,
    appState: AppState = rememberAppNavigationState()
) {
    val currentDestination = appState.rememberTopDestination()
    val navController = appState.navController

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(R.string.headline_home)) },
                selected = currentDestination == Home,
                onClick = {
                    navController.popBackStack<Home>(inclusive = false)
                }
            )
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(R.string.headline_settings)) },
                selected = currentDestination == Settings,
                onClick = {
                    if (currentDestination == Settings) {
                        navController.popBackStack<SettingsHome>(inclusive = false)
                    } else {
                        navController.navigate(Settings) {
                            launchSingleTop = true
                            popUpTo(Settings) {
                                inclusive = false
                            }
                        }
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = modifier
        ) {
            composable<Home> {
                HomePage()
            }
            navigation<Settings>(
                startDestination = SettingsHome
            ) {
                composable<SettingsHome> {
                    SettingsPage(
                        onBackgroundServices = {
                            // TODO
                        },
                        onNotifications = {
                            navController.navigate(NotificationsSettings) {
                                launchSingleTop = true
                            }
                        },
                        onLanguage = {
                            navController.navigate(LanguageSettings) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable<NotificationsSettings> {
                    NotificationsPage(
                        onBack = {
                            navController.popBackStack<NotificationsSettings>(inclusive = true)
                        }
                    )
                }
                composable<LanguageSettings> {
                    LanguagePage(
                        onBack = {
                            navController.popBackStack<LanguageSettings>(inclusive = true)
                        }
                    )
                }
            }
        }
    }
}
