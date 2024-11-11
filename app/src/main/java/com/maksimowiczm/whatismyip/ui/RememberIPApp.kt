package com.maksimowiczm.whatismyip.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maksimowiczm.whatismyip.currentaddress.CurrentAddressScreen
import com.maksimowiczm.whatismyip.ui.theme.RememberIPTheme
import kotlinx.serialization.Serializable

@Composable
fun RememberIPApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    RememberIPTheme {
        Scaffold(modifier) { innerPadding ->
            Column(Modifier.padding(innerPadding)) {
                NavHost(
                    navController = navController,
                    startDestination = CurrentAddressRoute
                ) {
                    composable<CurrentAddressRoute> { CurrentAddressScreen() }
                }
            }
        }
    }
}

@Serializable
data object CurrentAddressRoute
