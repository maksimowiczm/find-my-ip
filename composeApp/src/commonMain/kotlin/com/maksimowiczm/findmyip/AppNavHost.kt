package com.maksimowiczm.findmyip

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.maksimowiczm.findmyip.navigation.forwardBackwardComposable
import com.maksimowiczm.findmyip.ui.contribute.ContributeRoute
import com.maksimowiczm.findmyip.ui.home.HomeRoute
import com.maksimowiczm.findmyip.ui.language.LanguageRoute
import com.maksimowiczm.findmyip.ui.settings.SettingsRoute

@Composable
fun AppNavHost(modifier: Modifier = Modifier.Companion) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Home.name,
        modifier = modifier,
    ) {
        forwardBackwardComposable(Route.Home.name) {
            HomeRoute(
                onSettings = {
                    navController.navigate(Route.Settings.name) { launchSingleTop = true }
                },
                onVolunteer = {
                    navController.navigate(Route.Contribute.name) { launchSingleTop = true }
                },
                modifier = Modifier,
            )
        }
        forwardBackwardComposable(Route.Settings.name) {
            SettingsRoute(
                onBack = { navController.popBackStack(Route.Settings.name, inclusive = true) },
                onContribute = {
                    navController.navigate(Route.Contribute.name) { launchSingleTop = true }
                },
                onLanguage = {
                    navController.navigate(Route.Language.name) { launchSingleTop = true }
                },
                modifier = Modifier,
            )
        }
        forwardBackwardComposable(Route.Contribute.name) {
            ContributeRoute(
                onBack = { navController.popBackStack(Route.Contribute.name, inclusive = true) }
            )
        }
        forwardBackwardComposable(Route.Language.name) {
            LanguageRoute(
                onBack = { navController.popBackStack(Route.Language.name, inclusive = true) }
            )
        }
    }
}

private enum class Route {
    Home,
    Settings,
    Contribute,
    Language,
}
