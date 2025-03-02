package com.maksimowiczm.findmyip

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.maksimowiczm.findmyip.FindMyIpAppState
import com.maksimowiczm.findmyip.feature.addresshistory.addressHistoryGraph
import com.maksimowiczm.findmyip.feature.currentaddress.CurrentAddress
import com.maksimowiczm.findmyip.feature.currentaddress.currentAddressGraph
import com.maksimowiczm.findmyip.settings.Setting
import com.maksimowiczm.findmyip.settings.SettingsRoute
import com.maksimowiczm.findmyip.settings.navigateAddressHistoryAdvancedSettings
import com.maksimowiczm.findmyip.settings.navigateSettingsHome
import com.maksimowiczm.findmyip.settings.settingsGraph

@Composable
fun FindMyIpNavHost(appState: FindMyIpAppState, modifier: Modifier = Modifier) {
    val navController = appState.navController

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = CurrentAddress
    ) {
        currentAddressGraph()

        addressHistoryGraph(
            onGrantPermission = { navController.navigateSettingsHome(Setting.SaveHistory) }
        )

        settingsGraph(
            onAddressHistoryAdvancedSettings =
            navController::navigateAddressHistoryAdvancedSettings,
            onAddressHistoryAdvancedSettingsNavigateBack = {
                navController.navigateSettingsHome(
                    navOptions = navOptions {
                        popUpTo(SettingsRoute) { inclusive = false }
                    }
                )
            }
        )
    }
}
