package com.maksimowiczm.findmyip.feature.addresshistory

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.are_you_sure
import findmyip.composeapp.generated.resources.cancel
import findmyip.composeapp.generated.resources.history_no_permission_dialog_description
import findmyip.composeapp.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

@Composable
fun HistoryPermissionDialog(onDismiss: () -> Unit, onGrantPermission: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.are_you_sure)) },
        text = {
            Text(
                text = stringResource(Res.string.history_no_permission_dialog_description),
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
            TextButton(onClick = onGrantPermission) {
                Text(stringResource(Res.string.ok))
            }
        }
    )
}
