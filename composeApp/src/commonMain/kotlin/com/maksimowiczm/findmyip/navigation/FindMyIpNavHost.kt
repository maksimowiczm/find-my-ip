package com.maksimowiczm.findmyip.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maksimowiczm.findmyip.feature.currentaddress.CurrentAddressScreen
import kotlinx.serialization.Serializable

sealed interface Destination

@Serializable
data object CurrentAddress : Destination

@Composable
fun FindMyIpNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = CurrentAddress,
        modifier = modifier
    ) {
        composable<CurrentAddress> {
            CurrentAddressScreen()
        }
    }
}
