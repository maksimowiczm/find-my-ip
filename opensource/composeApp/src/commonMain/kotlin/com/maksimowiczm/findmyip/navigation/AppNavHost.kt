package com.maksimowiczm.findmyip.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.maksimowiczm.findmyip.feature.background.ui.RunInBackgroundRoute
import com.maksimowiczm.findmyip.feature.contibute.ui.ContributeRoute
import com.maksimowiczm.findmyip.feature.sponsor.ui.SponsorRoute
import com.maksimowiczm.findmyip.shared.core.navigation.forwardBackwardComposable
import com.maksimowiczm.findmyip.shared.feature.home.ui.HomeRoute
import com.maksimowiczm.findmyip.shared.feature.language.ui.LanguageRoute
import com.maksimowiczm.findmyip.shared.feature.settings.ui.SettingsRoute

@Composable
internal fun AppNavHost(modifier: Modifier = Modifier.Companion) {
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
                onRunInBackground = {
                    navController.navigate(Route.Background.name) { launchSingleTop = true }
                },
                onLanguage = {
                    navController.navigate(Route.Language.name) { launchSingleTop = true }
                },
                modifier = Modifier,
            )
        }
        forwardBackwardComposable(Route.Contribute.name) {
            ContributeRoute(
                onBack = { navController.popBackStack(Route.Contribute.name, inclusive = true) },
                onSponsor = {
                    navController.navigate(Route.Sponsor.name) { launchSingleTop = true }
                },
            )
        }
        forwardBackwardComposable(Route.Language.name) {
            LanguageRoute(
                onBack = { navController.popBackStack(Route.Language.name, inclusive = true) }
            )
        }
        forwardBackwardComposable(Route.Sponsor.name) {
            SponsorRoute(
                onBack = { navController.popBackStack(Route.Sponsor.name, inclusive = true) }
            )
        }
        forwardBackwardComposable(Route.Background.name) {
            RunInBackgroundRoute(
                onBack = { navController.popBackStack(Route.Background.name, inclusive = true) }
            )
        }
    }
}

private enum class Route {
    Home,
    Settings,
    Contribute,
    Language,
    Sponsor,
    Background,
}
