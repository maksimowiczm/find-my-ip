package com.maksimowiczm.findmyip.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.history_no_permission_description
import findmyip.composeapp.generated.resources.tap_to_enable
import org.jetbrains.compose.resources.stringResource

@Composable
fun HistoryDisabledScreen(onGrantPermission: () -> Unit, modifier: Modifier = Modifier) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        EnableHistoryAlertDialog(
            onDismissRequest = { showDialog = false },
            onConfirm = {
                showDialog = false
                onGrantPermission()
            }
        )
    }

    Surface(
        onClick = { showDialog = true },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Res.string.history_no_permission_description),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.tap_to_enable),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
