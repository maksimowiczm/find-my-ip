package com.maksimowiczm.findmyip.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.addresshistory.AddressHistory
import com.maksimowiczm.findmyip.currentaddress.CurrentAddress
import com.maksimowiczm.findmyip.navigation.FindMyIpNavHost
import com.maksimowiczm.findmyip.settings.SettingsHome
import com.maksimowiczm.findmyip.settings.SettingsRoute

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
                        painter = painterResource(R.drawable.ic_home_24),
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(R.string.home)) },
                selected = currentDestination.isRouteInHierarchy<CurrentAddress>(),
                onClick = { appState.navigateToTopLevelDestination(CurrentAddress) }
            )
            item(
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_history_24),
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(R.string.history)) },
                selected = currentDestination.isRouteInHierarchy<AddressHistory>(),
                onClick = { appState.navigateToTopLevelDestination(AddressHistory) }
            )
            item(
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_settings_24),
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(R.string.settings)) },
                selected = currentDestination.isRouteInHierarchy<SettingsRoute>(),
                onClick = { appState.navigateToTopLevelDestination(SettingsHome(null)) }
            )
        }
    ) {
        FindMyIpNavHost(appState)
    }
}
