package com.maksimowiczm.findmyip.ui

import androidx.compose.animation.AnimatedContentTransitionScope.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maksimowiczm.findmyip.addresshistory.AddressHistoryScreen
import com.maksimowiczm.findmyip.currentaddress.CurrentAddressScreen
import com.maksimowiczm.findmyip.settings.SettingsNavigation
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme

@Composable
internal fun FindMyIpApp() {
    FindMyIpAppTheme {
        Surface {
            FindMyIpAppContent()
        }
    }
}

@Composable
private fun FindMyIpAppContent() {
    val navController = rememberNavController()
    var currentRoute by remember { mutableStateOf<Route.Variant?>(null) }

    Column(Modifier.fillMaxSize()) {
        NavHost(
            modifier = Modifier.weight(1f),
            navController = navController,
            startDestination = Route.CurrentAddress
        ) {
            composable<Route.CurrentAddress>(
                enterTransition = { slideIntoContainer(SlideDirection.Up) },
                exitTransition = { slideOutOfContainer(SlideDirection.Up) }
            ) {
                currentRoute = Route.Variant.CurrentAddress
                CurrentAddressScreen(Modifier.fillMaxSize())
            }

            composable<Route.AddressHistory>(
                enterTransition = { slideIntoContainer(SlideDirection.Up) },
                exitTransition = { slideOutOfContainer(SlideDirection.Up) }
            ) {
                currentRoute = Route.Variant.AddressHistory
                AddressHistoryScreen(Modifier.fillMaxSize())
            }

            composable<Route.Settings>(
                enterTransition = { slideIntoContainer(SlideDirection.Up) },
                exitTransition = { slideOutOfContainer(SlideDirection.Up) }
            ) {
                currentRoute = Route.Variant.Settings
                SettingsNavigation(Modifier.fillMaxSize())
            }
        }

        FindMyIpBottomAppBar(
            selectedBottomBarItem = currentRoute,
            onHomeClick = { navController.navigateSingleTop(Route.CurrentAddress) },
            onAddressHistoryClick = { navController.navigateSingleTop(Route.AddressHistory) },
            onSettingsClick = { navController.navigateSingleTop(Route.Settings) }
        )
    }
}

fun NavController.navigateSingleTop(route: Any) {
    navigate(route) {
        launchSingleTop = true
    }
}
