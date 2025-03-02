package com.maksimowiczm.findmyip.feature.settings.addresshistory

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.maksimowiczm.findmyip.feature.settings.SettingClickable
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.are_you_sure
import findmyip.composeapp.generated.resources.cancel
import findmyip.composeapp.generated.resources.clear
import findmyip.composeapp.generated.resources.history_clear
import findmyip.composeapp.generated.resources.history_clear_description
import findmyip.composeapp.generated.resources.history_clear_dialog_description
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ClearHistorySetting(
    modifier: Modifier = Modifier,
    viewModel: AddressHistorySettingsViewModel = koinViewModel()
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
        headlineContent = { Text(text = stringResource(Res.string.history_clear)) },
        supportingContent = { Text(stringResource(Res.string.history_clear_description)) },
        modifier = modifier
    )
}

@Composable
private fun HistoryClearDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.are_you_sure)) },
        text = {
            Text(
                text = stringResource(Res.string.history_clear_dialog_description),
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
                Text(stringResource(Res.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onConfirm) {
                Text(stringResource(Res.string.clear))
            }
        }
    )
}
