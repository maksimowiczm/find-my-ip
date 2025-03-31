@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.ui

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
import com.maksimowiczm.findmyip._2.navigation.TopRoute

@Composable
fun rememberFindMyIpAppState(
    navController: NavHostController = rememberNavController()
): FindMyIpAppState = remember(navController) {
    FindMyIpAppState(
        navController = navController
    )
}

@Stable
class FindMyIpAppState(val navController: NavHostController) {
    val currentTopRoute: TopRoute?
        @Composable get() {
            val destination = navController.currentBackStackEntryAsState().value?.destination

            return when {
                destination.isRouteInHierarchy<TopRoute.Home>() -> TopRoute.Home
                destination.isRouteInHierarchy<TopRoute.History>() -> TopRoute.History
                destination.isRouteInHierarchy<TopRoute.Settings>() -> TopRoute.Settings
                else -> null
            }
        }

    inline fun <reified T : TopRoute> navigate(destination: T) {
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

inline fun <reified T : Any> NavDestination?.isRouteInHierarchy(): Boolean = this?.hierarchy?.any {
    it.hasRoute<T>()
} == true
