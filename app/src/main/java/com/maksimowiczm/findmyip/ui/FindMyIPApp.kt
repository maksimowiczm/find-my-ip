package com.maksimowiczm.findmyip.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.navigation.FindMyIpNavHost
import com.maksimowiczm.findmyip.navigation.History
import com.maksimowiczm.findmyip.navigation.Home
import com.maksimowiczm.findmyip.navigation.Settings
import com.maksimowiczm.findmyip.navigation.SettingsHome
import com.maksimowiczm.findmyip.navigation.rememberAppNavigationState

@Composable
fun FindMyIPApp(modifier: Modifier = Modifier) {
    val appNavigationState = rememberAppNavigationState()
    val currentDestination = appNavigationState.rememberTopDestination()
    val navController = appNavigationState.navController

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
                    navController.popBackStack<Home>(inclusive = false, saveState = true)
                }
            )
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(R.string.headline_history)) },
                selected = currentDestination == History,
                onClick = {
                    if (currentDestination == History) {
                        navController.popBackStack<History>(inclusive = false)
                    } else {
                        navController.navigate(History) {
                            launchSingleTop = true
                            restoreState = true

                            popUpTo<Home> {
                                saveState = true
                            }
                        }
                    }
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
                            restoreState = true

                            popUpTo<Home> {
                                saveState = true
                            }
                        }
                    }
                }
            )
        }
    ) {
        FindMyIpNavHost(
            modifier = modifier,
            appNavigationState = appNavigationState
        )
    }
}
