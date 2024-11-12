package com.maksimowiczm.whatismyip.addresshistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.whatismyip.R
import com.maksimowiczm.whatismyip.ui.theme.WhatsMyIpAppTheme

@Composable
fun HistorySettings(
    modifier: Modifier = Modifier,
    viewModel: HistorySettingsViewModel = hiltViewModel()
) {
    val state by viewModel.historySettingsState.collectAsStateWithLifecycle()
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        PermissionDialog(
            onDismiss = { showDialog = false },
            onGrantPermission = {
                viewModel.enableHistorySettings()
                showDialog = false
            }
        )
    }

    HistorySettings(
        modifier = modifier,
        state = state,
        onToggle = {
            if (!state) {
                showDialog = true
            } else {
                viewModel.disableHistorySettings()
            }
        },
        onClear = viewModel::clearHistory
    )
}

@Composable
private fun HistorySettings(
    state: HistorySettingsState,
    onToggle: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.history),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.history_settings_description),
            style = MaterialTheme.typography.bodyMedium
        )
        HistoryEnabledItem(
            state = state,
            onToggle = onToggle
        )
        ClearHistoryItem(
            onClear = onClear
        )
    }
}

@Composable
private fun HistorySettingsItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (BoxScope.() -> Unit)
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        content()
    }
}

@Composable
private fun HistoryEnabledItem(
    state: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    HistorySettingsItem(
        onClick = onToggle,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.save_history),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = state,
                onCheckedChange = { onToggle() }
            )
        }
    }
}

@Composable
private fun ClearHistoryItem(onClear: () -> Unit, modifier: Modifier = Modifier) {
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

    HistorySettingsItem(
        onClick = { showDialog = true },
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(R.string.clear_history),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun HistoryClearDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.are_you_sure)) },
        text = {
            Text(
                text = stringResource(R.string.clear_history_dialog_text),
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
    WhatsMyIpAppTheme {
        Surface {
            HistorySettings(
                state = true,
                onToggle = {},
                onClear = {}
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun HistoryClearDialogPreview() {
    WhatsMyIpAppTheme {
        Surface {
            HistoryClearDialog(
                onDismiss = {},
                onConfirm = {}
            )
        }
    }
}
