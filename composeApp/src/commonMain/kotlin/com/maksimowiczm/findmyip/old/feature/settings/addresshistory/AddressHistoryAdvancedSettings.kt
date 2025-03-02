package com.maksimowiczm.findmyip.old.feature.settings.addresshistory

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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.back
import findmyip.composeapp.generated.resources.history_disabled_description
import findmyip.composeapp.generated.resources.off
import findmyip.composeapp.generated.resources.on
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddressHistoryAdvancedSettings(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddressHistorySettingsViewModel = koinViewModel()
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
//        WorkerSettings()
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
                        contentDescription = stringResource(Res.string.back)
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
                    Text(stringResource(Res.string.on))
                } else {
                    Text(stringResource(Res.string.off))
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
                        text = stringResource(Res.string.history_disabled_description),
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
