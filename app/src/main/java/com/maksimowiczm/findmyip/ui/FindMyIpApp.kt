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
import com.maksimowiczm.findmyip.settings.SettingsScreen
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme

@Composable
fun FindMyIpApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route?.let {
            Route.fromRoute(it)
        }

    val onHomeClick = {
        if (currentRoute != Route.CurrentAddress) {
            navController.navigateSingleTop(CurrentAddressRoute)
        }
    }
    val onAddressHistoryClick = {
        if (currentRoute != Route.AddressHistory) {
            navController.navigateSingleTop(AddressHistoryRoute)
        }
    }
    val onSettingsClick = {
        if (currentRoute != Route.Settings) {
            navController.navigateSingleTop(SettingsRoute)
        }
    }

    FindMyIpAppTheme {
        Scaffold(
            modifier = modifier,
            bottomBar = {
                FindMyIpBottomAppBar(
                    selectedBottomBarItem = currentRoute,
                    onHomeClick = onHomeClick,
                    onAddressHistoryClick = onAddressHistoryClick,
                    onSettingsClick = onSettingsClick
                )
            }
        ) { innerPadding ->
            NavHost(
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
                    CurrentAddressScreen(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
                composable<AddressHistoryRoute>(
                    enterTransition = {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
                    },
                    exitTransition = {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
                    }
                ) {
                    AddressHistoryScreen(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
                composable<SettingsRoute>(
                    enterTransition = {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
                    },
                    exitTransition = {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
                    }
                ) {
                    SettingsScreen(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
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
