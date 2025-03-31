@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.ui.settings.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import findmyip.composeapp.generated.resources.*
import findmyip.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HistorySettings(
    onAdvancedSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistorySettingsViewModel = koinViewModel()
) {
    val isEnabled by viewModel.isEnabled.collectAsStateWithLifecycle()

    HistorySettings(
        onAdvancedSettingsClick = onAdvancedSettingsClick,
        checked = isEnabled,
        onCheckedChange = viewModel::onEnableChange,
        onHistoryClear = viewModel::clearHistory,
        modifier = modifier
    )
}

@Composable
private fun HistorySettings(
    onAdvancedSettingsClick: () -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onHistoryClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(Res.string.headline_history),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        ListItem(
            headlineContent = {
                Text(stringResource(Res.string.headline_save_history))
            },
            modifier = Modifier.clickable { onAdvancedSettingsClick() },
            supportingContent = {
                Text(stringResource(Res.string.description_save_history))
            },
            trailingContent = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    VerticalDivider(
                        modifier = Modifier
                            .height(25.dp)
                            .padding(horizontal = 16.dp)
                    )
                    Switch(
                        checked = checked,
                        onCheckedChange = onCheckedChange
                    )
                }
            }
        )
        ClearHistoryListItem(
            onClear = onHistoryClear
        )
    }
}

@Composable
private fun ClearHistoryListItem(onClear: () -> Unit, modifier: Modifier = Modifier) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        HistoryClearDialog(
            onDismiss = { showDialog = false },
            onConfirm = {
                onClear()
                showDialog = false
            }
        )
    }

    ListItem(
        headlineContent = {
            Text(stringResource(Res.string.headline_clear_history))
        },
        modifier = modifier.clickable { showDialog = true },
        supportingContent = {
            Text(stringResource(Res.string.description_clear_history))
        }
    )
}

@Composable
private fun HistoryClearDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.headline_clear_history)) },
        text = {
            Text(
                text = stringResource(Res.string.description_clear_history_dialog),
                textAlign = TextAlign.Justify
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(Res.string.action_cancel))
            }
        },
        confirmButton = {
            TextButton(onConfirm) {
                Text(stringResource(Res.string.action_clear))
            }
        }
    )
}
