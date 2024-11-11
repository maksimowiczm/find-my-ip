package com.maksimowiczm.whatismyip.addresshistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
fun AddressHistoryScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: AddressHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val state = uiState

    when (state) {
        AddressHistoryState.NoPermission -> NoPermissionScreen(
            modifier = modifier,
            onGrantPermission = viewModel::onGrantPermission
        )

        AddressHistoryState.Loading -> Box(modifier) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun NoPermissionScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    onGrantPermission: () -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        PermissionDialog(
            onDismiss = { showDialog = false },
            onGrantPermission = onGrantPermission
        )
    }

    Column(
        modifier = modifier.clickable { showDialog = true },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.history_no_permission_screen_text),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.tap_to_enable),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun PermissionDialog(onDismiss: () -> Unit, onGrantPermission: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.are_you_sure)) },
        text = {
            Text(
                text = stringResource(R.string.history_no_permission_dialog_text),
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
            TextButton(onClick = onGrantPermission) {
                Text(stringResource(R.string.ok))
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun AddressHistoryScreenPreview() {
    WhatsMyIpAppTheme {
        Surface {
            NoPermissionScreen(
                onGrantPermission = {}
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PermissionDialogPreview() {
    WhatsMyIpAppTheme {
        Surface {
            PermissionDialog(
                onDismiss = {},
                onGrantPermission = {}
            )
        }
    }
}
