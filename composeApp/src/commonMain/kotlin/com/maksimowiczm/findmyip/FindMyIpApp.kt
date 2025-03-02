package com.maksimowiczm.findmyip

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.maksimowiczm.findmyip.feature.addresshistory.AddressHistory
import com.maksimowiczm.findmyip.feature.currentaddress.CurrentAddress
import com.maksimowiczm.findmyip.settings.SettingsHome
import com.maksimowiczm.findmyip.settings.SettingsRoute
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.history
import findmyip.composeapp.generated.resources.home
import findmyip.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun FindMyIpApp(modifier: Modifier = Modifier) {
    val appState = rememberFindMyIpAppState()
    val currentDestination = appState.currentDestination

    NavigationSuiteScaffold(
        modifier = modifier,
        navigationSuiteItems = {
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(Res.string.home)) },
                selected = currentDestination.isRouteInHierarchy<CurrentAddress>(),
                onClick = { appState.navigateToTopLevelDestination(CurrentAddress) }
            )
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(Res.string.history)) },
                selected = currentDestination.isRouteInHierarchy<AddressHistory>(),
                onClick = { appState.navigateToTopLevelDestination(AddressHistory) }
            )
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(Res.string.settings)) },
                selected = currentDestination.isRouteInHierarchy<SettingsRoute>(),
                onClick = { appState.navigateToTopLevelDestination(SettingsHome(null)) }
            )
        }
    ) {
        FindMyIpNavHost(appState)
    }
}
