package com.maksimowiczm.findmyip.ui.page.history

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.maksimowiczm.findmyip.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        confirmButton = {
            TextButton(
                onClick = onDelete
            ) {
                Text(stringResource(R.string.action_delete))
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = null
            )
        },
        title = {
            Text(stringResource(R.string.action_delete_entry))
        },
        text = {
            Text(stringResource(R.string.description_delete_entry))
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    )
}
