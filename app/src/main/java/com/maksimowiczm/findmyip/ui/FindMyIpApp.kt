package com.maksimowiczm.findmyip.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
internal fun FindMyIpApp() {
    val navController = rememberNavController()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route?.let {
            Route.Companion.fromRoute(it)
        }

    FindMyIpAppTheme {
        Scaffold(
            bottomBar = {
                FindMyIpBottomAppBar(
                    selectedBottomBarItem = currentRoute,
                    onHomeClick = { navController.navigateSingleTop(CurrentAddressRoute) },
                    onAddressHistoryClick = {
                        navController.navigateSingleTop(AddressHistoryRoute)
                    },
                    onSettingsClick = { navController.navigateSingleTop(SettingsRoute) }
                )
            }
        ) { innerPadding ->
            NavHost(
                modifier = Modifier.padding(innerPadding),
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
        }
    }
}

fun NavController.navigateSingleTop(route: Any) {
    navigate(route) {
        launchSingleTop = true
    }
}
