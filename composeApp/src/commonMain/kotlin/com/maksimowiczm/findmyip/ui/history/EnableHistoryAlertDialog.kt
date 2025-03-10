package com.maksimowiczm.findmyip.ui.history

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import findmyip.composeapp.generated.resources.*
import findmyip.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EnableHistoryAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(Res.string.headline_enable_history)) },
        text = {
            Text(
                text = stringResource(Res.string.description_enable_history),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.action_cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(Res.string.action_enable))
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
