package com.maksimowiczm.findmyip.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.maksimowiczm.findmyip.navigation.FindMyIpNavHost

@Composable
fun FindMyIpApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    FindMyIpNavHost(
        modifier = modifier,
        navController = navController
    )
}
