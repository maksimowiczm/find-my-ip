package com.maksimowiczm.findmyip.settings.addresshistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.addresshistory.HistoryPermissionDialog
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme

@Composable
internal fun HistorySettings(
    viewModel: HistorySettingsViewModel = hiltViewModel(),
    onHistorySettingsClick: () -> Unit
) {
    val state by viewModel.saveHistoryState.collectAsStateWithLifecycle()

    HistorySettings(
        checked = state,
        onCheckedChange = {
            if (it) {
                viewModel.enableHistorySettings()
            } else {
                viewModel.disableHistorySettings()
            }
        },
        onAdvancedSettingsClick = onHistorySettingsClick,
        onHistoryClear = viewModel::clearHistory
    )
}

@Composable
private fun HistorySettings(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onAdvancedSettingsClick: () -> Unit,
    onHistoryClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(checked) {
        if (checked) {
            showDialog = false
        }
    }

    if (showDialog) {
        HistoryPermissionDialog(
            onDismiss = { showDialog = false },
            onGrantPermission = { onCheckedChange(true) }
        )
    }

    Column(modifier) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.history),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.history_save_description),
            style = MaterialTheme.typography.bodyMedium
        )
        ListItem(
            modifier = Modifier.clickable(onClick = onAdvancedSettingsClick),
            headlineContent = { Text(stringResource(R.string.history_save)) },
            supportingContent = {
                Text(
                    stringResource(R.string.history_save_description)
                )
            },
            trailingContent = {
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        if (it) {
                            showDialog = true
                        } else {
                            onCheckedChange(it)
                        }
                    }
                )
            }
        )
        ClearHistoryItem(onHistoryClear)
    }
}

@Composable
private fun ClearHistoryItem(onClearHistory: () -> Unit, modifier: Modifier = Modifier) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        HistoryClearDialog(
            onDismiss = { showDialog = false },
            onConfirm = {
                onClearHistory()
                showDialog = false
            }
        )
    }

    ListItem(
        modifier = modifier.clickable(onClick = { showDialog = true }),
        headlineContent = {
            Text(
                text = stringResource(R.string.history_clear)
            )
        },
        supportingContent = {
            Text(stringResource(R.string.history_clear_description))
        }
    )
}

@Composable
private fun HistoryClearDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.are_you_sure)) },
        text = {
            Text(
                text = stringResource(R.string.history_clear_dialog_description),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.error,
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onConfirm) {
                Text(stringResource(R.string.clear))
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun HistorySettingsPreview() {
    FindMyIpAppTheme {
        Surface {
            HistorySettings(
                checked = true,
                onCheckedChange = {},
                onHistoryClear = {},
                onAdvancedSettingsClick = {}
            )
        }
    }
}
