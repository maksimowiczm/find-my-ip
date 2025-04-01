package com.maksimowiczm.findmyip.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maksimowiczm.findmyip.feature.currentaddress.CurrentAddressScreen
import com.maksimowiczm.findmyip.feature.settings.settingsGraph
import com.maksimowiczm.findmyip.ui.FindMyIpAppState
import com.maksimowiczm.findmyip.ui.motion.materialFadeThroughIn
import com.maksimowiczm.findmyip.ui.motion.materialFadeThroughOut
import com.maksimowiczm.findmyip.ui.rememberFindMyIpAppState
import findmyip.composeapp.generated.resources.*
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
sealed interface Destination

@Serializable
data object CurrentAddress : Destination

@Serializable
data object History : Destination

@Serializable
data object Settings : Destination

@Composable
fun FindMyIpNavHost(
    modifier: Modifier = Modifier,
    appState: FindMyIpAppState = rememberFindMyIpAppState(rememberNavController())
) {
    val selectedTopRoute = appState.currentTopRoute

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = stringResource(Res.string.action_go_to_home)
                    )
                },
                label = { Text(stringResource(Res.string.headline_home)) },
                selected = selectedTopRoute == CurrentAddress,
                onClick = { appState.navigate(CurrentAddress) }
            )
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = stringResource(Res.string.action_go_to_history)
                    )
                },
                label = { Text(stringResource(Res.string.headline_history)) },
                selected = selectedTopRoute == History,
                onClick = { appState.navigate(History) }
            )
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(Res.string.action_go_to_settings)
                    )
                },
                label = { Text(stringResource(Res.string.headline_settings)) },
                selected = selectedTopRoute == Settings,
                onClick = { appState.navigate(Settings) }
            )
        }
    ) {
        NavHost(
            navController = appState.navController,
            startDestination = CurrentAddress,
            modifier = modifier
        ) {
            composable<CurrentAddress>(
                enterTransition = { materialFadeThroughIn() },
                exitTransition = { materialFadeThroughOut() }
            ) {
                CurrentAddressScreen()
            }
            composable<History>(
                enterTransition = { materialFadeThroughIn() },
                exitTransition = { materialFadeThroughOut() }
            ) {
                // TODO
                Text("History")
            }
            settingsGraph<Settings>(
                navController = appState.navController,
                enterTransition = { materialFadeThroughIn() },
                exitTransition = { materialFadeThroughOut() }
            )
        }
    }
}
