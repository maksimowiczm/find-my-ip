package com.maksimowiczm.findmyip.ui.history

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.are_you_sure
import findmyip.composeapp.generated.resources.cancel
import findmyip.composeapp.generated.resources.history_no_permission_dialog_description
import findmyip.composeapp.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

@Composable
fun EnableHistoryAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(Res.string.are_you_sure)) },
        text = {
            Text(
                text = stringResource(Res.string.history_no_permission_dialog_description),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(Res.string.ok))
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun EnableHistoryAlertDialogPreview() {
    EnableHistoryAlertDialog(
        onDismissRequest = {},
        onConfirm = {}
    )
}
