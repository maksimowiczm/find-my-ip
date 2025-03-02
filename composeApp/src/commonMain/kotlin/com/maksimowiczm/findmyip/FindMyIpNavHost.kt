package com.maksimowiczm.findmyip

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maksimowiczm.findmyip.ui.home.HomeScreen
import com.maksimowiczm.findmyip.ui.settings.SettingsScreen
import kotlinx.serialization.Serializable

sealed interface TopRoute {
    @Serializable
    data object Home : TopRoute

    @Serializable
    data object History : TopRoute

    @Serializable
    data object Settings : TopRoute
}

@Composable
fun FindMyIpNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TopRoute.Home
    ) {
        composable<TopRoute.Home> {
            HomeScreen()
        }
        composable<TopRoute.History> { Surface { Spacer(Modifier.fillMaxSize()) } }
        composable<TopRoute.Settings> {
            SettingsScreen()
        }
    }
}
