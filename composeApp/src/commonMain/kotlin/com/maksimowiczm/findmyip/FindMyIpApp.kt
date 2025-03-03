package com.maksimowiczm.findmyip

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.history
import findmyip.composeapp.generated.resources.home
import findmyip.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.stringResource

@Composable
fun FindMyIpApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val appState = rememberFindMyIpAppState(navController)
    val selectedTopRoute = appState.currentTopRoute

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(Res.string.home)) },
                selected = selectedTopRoute == TopRoute.Home,
                onClick = { appState.navigate(TopRoute.Home) }
            )
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(Res.string.history)) },
                selected = selectedTopRoute == TopRoute.History,
                onClick = { appState.navigate(TopRoute.History) }
            )
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(Res.string.settings)) },
                selected = selectedTopRoute == TopRoute.Settings,
                onClick = { appState.navigate(TopRoute.Settings) }
            )
        }
    ) {
        Scaffold(
            modifier = modifier,
            contentWindowInsets = WindowInsets(0.dp)
        ) { paddingValues ->
            FindMyIpNavHost(
                navController = navController,
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
            )
        }
    }
}
