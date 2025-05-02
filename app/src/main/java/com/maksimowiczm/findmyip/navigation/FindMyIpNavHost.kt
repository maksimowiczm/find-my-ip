package com.maksimowiczm.findmyip.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maksimowiczm.findmyip.ui.page.home.HomePage
import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination

@Serializable
data object Home : Destination

@Composable
fun FindMyIpNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {
        composable<Home> {
            HomePage()
        }
    }
}
