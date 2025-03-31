package com.maksimowiczm.findmyip.ui.settings.autorefresh

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
fun AutoRefreshSettingsListItem(
    modifier: Modifier = Modifier,
    viewModel: AutoRefreshSettingsViewModel = koinViewModel()
) {
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()

    AutoRefreshSettingsListItem(
        enabled = enabled,
        onEnabledChange = viewModel::setEnabled,
        modifier = modifier
    )
}

@Composable
private fun AutoRefreshSettingsListItem(
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(stringResource(Res.string.headline_auto_refresh)) },
        modifier = modifier.clickable { onEnabledChange(!enabled) },
        supportingContent = { Text(stringResource(Res.string.description_auto_refresh)) },
        trailingContent = {
            Switch(
                checked = enabled,
                onCheckedChange = onEnabledChange
            )
        }
    )
}
