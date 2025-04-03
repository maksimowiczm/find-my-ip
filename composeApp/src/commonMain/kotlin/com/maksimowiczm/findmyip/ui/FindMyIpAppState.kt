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
import com.maksimowiczm.findmyip.navigation.CurrentAddress
import com.maksimowiczm.findmyip.navigation.Destination
import com.maksimowiczm.findmyip.navigation.History
import com.maksimowiczm.findmyip.navigation.Settings

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
    val currentTopRoute: Destination?
        @Composable get() {
            val destination = navController.currentBackStackEntryAsState().value?.destination

            return when {
                destination.isRouteInHierarchy<CurrentAddress>() -> CurrentAddress
                destination.isRouteInHierarchy<History>() -> History
                destination.isRouteInHierarchy<Settings>() -> Settings
                else -> null
            }
        }

    inline fun <reified T : Destination> navigate(destination: T) {
        val start = navController.graph.findStartDestination().id

        // TODO disable animation if destination is already on screen
//        val topDestination = navController.currentDestination?.hierarchy?.first()
//        if (topDestination.isRouteInHierarchy<T>()) {
//            return
//        }

        navController.navigate(
            route = destination,
            navOptions = navOptions {
                popUpTo(start) {
                }

                launchSingleTop = true
            }
        )
    }
}

inline fun <reified T : Any> NavDestination?.isRouteInHierarchy(): Boolean = this?.hierarchy?.any {
    it.hasRoute<T>()
} == true
