package com.maksimowiczm.findmyip.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions

@Composable
internal fun rememberFindMyIpAppState(
    navController: NavHostController = rememberNavController()
): FindMyIpAppState {
    return remember(navController) {
        FindMyIpAppState(
            navController = navController
        )
    }
}

@Stable
internal class FindMyIpAppState(
    val navController: NavHostController
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    inline fun <reified T : Any> navigateToTopLevelDestination(destination: T) {
        val start = navController.graph.findStartDestination().id

        val topDestination = navController.currentDestination?.hierarchy?.first()
        if (topDestination.isRouteInHierarchy<T>()) {
            return
        }

        navController.navigate(
            route = destination,
            navOptions = navOptions {
                popUpTo(start) {
                    saveState = true
                }

                launchSingleTop = true
                restoreState = true
            }
        )
    }
}

internal inline fun <reified T : Any> NavDestination?.isRouteInHierarchy(): Boolean {
    return this?.hierarchy?.any { it.hasRoute<T>() } == true
}
