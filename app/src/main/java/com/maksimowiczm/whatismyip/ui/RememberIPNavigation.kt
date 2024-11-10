package com.maksimowiczm.whatismyip.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maksimowiczm.whatismyip.current_address.CurrentAddressScreen
import kotlinx.serialization.Serializable

@Serializable
data object CurrentAddressRoute

@Composable
fun RememberIPNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CurrentAddressRoute,
    ) {
        composable<CurrentAddressRoute> { CurrentAddressScreen() }
    }
}
