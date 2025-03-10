package com.maksimowiczm.findmyip.ui.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navOptions
import com.maksimowiczm.findmyip.navigation.settingsComposable
import com.maksimowiczm.findmyip.ui.settings.autorefresh.AutoRefreshSettingsListItem
import com.maksimowiczm.findmyip.ui.settings.language.LanguageScreen
import com.maksimowiczm.findmyip.ui.settings.language.LanguageSettingsListItem
import findmyip.composeapp.generated.resources.*
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

actual fun buildPlatformSettings(navController: NavController): LazyListScope.() -> Unit = {
    item {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp),
            text = stringResource(Res.string.headline_platform_settings),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }

    item {
        AutoRefreshSettingsListItem()
    }

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
