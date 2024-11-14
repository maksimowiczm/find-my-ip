package com.maksimowiczm.findmyip.ui

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
import kotlinx.serialization.Serializable

@Composable
fun FindMyIpApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    val selectedBottomBarItem = when (currentRoute) {
        CurrentAddressRoute.javaClass.name -> BottomNavItem.Home
        AddressHistoryRoute.javaClass.name -> BottomNavItem.AddressHistory
        SettingsRoute.javaClass.name -> BottomNavItem.Settings
        else -> null
    }

    val onHomeClick = {
        if (selectedBottomBarItem != BottomNavItem.Home) {
            navController.navigateSingleTop(CurrentAddressRoute)
        }
    }
    val onAddressHistoryClick = {
        if (selectedBottomBarItem != BottomNavItem.AddressHistory) {
            navController.navigateSingleTop(AddressHistoryRoute)
        }
    }
    val onSettingsClick = {
        if (selectedBottomBarItem != BottomNavItem.Settings) {
            navController.navigateSingleTop(SettingsRoute)
        }
    }

    FindMyIpAppTheme {
        Scaffold(
            modifier = modifier,
            bottomBar = {
                FindMyIpBottomAppBar(
                    selectedBottomBarItem = selectedBottomBarItem,
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
                composable<CurrentAddressRoute> {
                    CurrentAddressScreen(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
                composable<AddressHistoryRoute> {
                    AddressHistoryScreen(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
                composable<SettingsRoute> {
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

@Serializable
data object CurrentAddressRoute

@Serializable
data object AddressHistoryRoute

@Serializable
data object SettingsRoute

fun NavController.navigateSingleTop(route: Any) {
    navigate(route) {
        launchSingleTop = true
    }
}
