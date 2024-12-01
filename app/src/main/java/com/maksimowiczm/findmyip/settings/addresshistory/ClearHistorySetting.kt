package com.maksimowiczm.findmyip.settings.addresshistory

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.settings.SettingClickable
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme

@Composable
internal fun ClearHistorySetting(
    modifier: Modifier = Modifier,
    viewModel: AddressHistorySettingsViewModel = hiltViewModel()
) {
    ClearHistorySetting(
        onClearHistory = viewModel::clearHistory,
        modifier = modifier
    )
}

@Composable
private fun ClearHistorySetting(onClearHistory: () -> Unit, modifier: Modifier = Modifier) {
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

    SettingClickable(
        onClick = { showDialog = true },
        headlineContent = { Text(text = stringResource(R.string.history_clear)) },
        supportingContent = { Text(stringResource(R.string.history_clear_description)) },
        modifier = modifier
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
private fun ClearHistorySettingPreview() {
    FindMyIpAppTheme {
        Surface {
            ClearHistorySetting(
                onClearHistory = {},
                modifier = Modifier
            )
        }
    }
}
