package com.maksimowiczm.findmyip.settings.addresshistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.settings.backgroundworker.WorkerSettings
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme

@Composable
internal fun AddressHistoryAdvancedSettings(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddressHistorySettingsViewModel = hiltViewModel()
) {
    val enabled by viewModel.saveHistoryState.collectAsStateWithLifecycle()

    AddressHistoryAdvancedSettings(
        enabled = enabled,
        onEnabledChange = {
            if (it) {
                viewModel.enableHistorySettings()
            } else {
                viewModel.disableHistorySettings()
            }
        },
        onNavigateBack = onNavigateBack,
        modifier = modifier
    ) {
        NetworkTypeSettings()
        HorizontalDivider()
        WorkerSettings()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddressHistoryAdvancedSettings(
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    Settings: @Composable (LazyItemScope.() -> Unit)
) {
    Column(modifier) {
        TopAppBar(
            title = { Text("Address history settings") },
            navigationIcon = {
                IconButton(onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        )
        ListItem(
            modifier = Modifier.clickable(onClick = { onEnabledChange(!enabled) }),
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            headlineContent = {
                if (enabled) {
                    Text(stringResource(R.string.on))
                } else {
                    Text(stringResource(R.string.off))
                }
            },
            trailingContent = {
                Switch(
                    checked = enabled,
                    onCheckedChange = onEnabledChange
                )
            }
        )
        LazyColumn {
            item {
                if (!enabled) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(R.string.history_disabled_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                } else {
                    Settings()
                }
            }
            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun AddressHistoryAdvancedSettingsPreview() {
    FindMyIpAppTheme {
        Surface {
            AddressHistoryAdvancedSettings(
                enabled = true,
                onNavigateBack = {},
                onEnabledChange = {},
                Settings = {
                    Text("There are advanced settings here.")
                }
            )
        }
    }
}
