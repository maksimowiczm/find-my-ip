package com.maksimowiczm.findmyip.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.maksimowiczm.findmyip.addresshistory.AddressHistoryScreen
import com.maksimowiczm.findmyip.currentaddress.CurrentAddressScreen
import com.maksimowiczm.findmyip.settings.SettingsNavigation
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme

@Composable
internal fun FindMyIpApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route?.let {
            Route.Companion.fromRoute(it)
        }

    FindMyIpAppTheme {
        Surface(modifier) {
            Column {
                NavHost(
                    modifier = Modifier.weight(1f),
                    navController = navController,
                    startDestination = CurrentAddressRoute
                ) {
                    composable<CurrentAddressRoute>(
                        enterTransition = {
                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
                        },
                        exitTransition = {
                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
                        }
                    ) {
                        CurrentAddressScreen(Modifier.fillMaxSize())
                    }
                    composable<AddressHistoryRoute>(
                        enterTransition = {
                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
                        },
                        exitTransition = {
                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
                        }
                    ) {
                        AddressHistoryScreen(Modifier.fillMaxSize())
                    }
                    composable<SettingsRoute>(
                        enterTransition = {
                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
                        },
                        exitTransition = {
                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
                        }
                    ) {
                        SettingsNavigation(Modifier.fillMaxSize())
                    }
                }
                FindMyIpBottomAppBar(
                    selectedBottomBarItem = currentRoute,
                    onHomeClick = { navController.navigateSingleTop(CurrentAddressRoute) },
                    onAddressHistoryClick = {
                        navController.navigateSingleTop(AddressHistoryRoute)
                    },
                    onSettingsClick = { navController.navigateSingleTop(SettingsRoute) }
                )
            }
        }
    }
}

fun NavController.navigateSingleTop(route: Any) {
    navigate(route) {
        launchSingleTop = true
    }
}
