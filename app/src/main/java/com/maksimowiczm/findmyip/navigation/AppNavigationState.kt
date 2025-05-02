package com.maksimowiczm.findmyip.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberAppNavigationState(
    navController: NavHostController = rememberNavController()
): AppNavigationState = AppNavigationState(navController)

class AppNavigationState(val navController: NavHostController) {
    @Composable
    fun rememberTopDestination(): TopDestination? {
        val backStack by navController.currentBackStackEntryAsState()
        val destination = backStack?.destination

        return when {
            destination.isRouteInHierarchy<Home>() == true -> Home
            destination.isRouteInHierarchy<Settings>() == true -> Settings
            else -> null
        }
    }
}

private inline fun <reified T : Any> NavDestination?.isRouteInHierarchy(): Boolean =
    this?.hierarchy?.any { it.hasRoute<T>() } == true
