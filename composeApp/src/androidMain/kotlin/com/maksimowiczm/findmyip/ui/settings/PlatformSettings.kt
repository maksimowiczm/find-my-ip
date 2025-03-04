package com.maksimowiczm.findmyip.ui.settings

import androidx.compose.foundation.lazy.LazyListScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navOptions
import com.maksimowiczm.findmyip.navigation.settingsComposable
import com.maksimowiczm.findmyip.ui.settings.language.LanguageScreen
import com.maksimowiczm.findmyip.ui.settings.language.LanguageSettingsListItem
import kotlinx.serialization.Serializable

actual fun buildPlatformSettings(navController: NavController): LazyListScope.() -> Unit = {
    item {
        LanguageSettingsListItem(
            onClick = {
                navController.navigate(
                    route = LanguageSettings,
                    navOptions = navOptions {
                        launchSingleTop = true
                    }
                )
            }
        )
    }
}

@Serializable
data object LanguageSettings

actual fun NavGraphBuilder.platformSettingsGraph(navController: NavController) {
    settingsComposable<LanguageSettings> {
        LanguageScreen(
            onNavigateUp = {
                navController.popBackStack<LanguageSettings>(inclusive = true)
            }
        )
    }
}
