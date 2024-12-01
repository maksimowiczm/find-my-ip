package com.maksimowiczm.findmyip.settings.addresshistory

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.addresshistory.HistoryPermissionDialog
import com.maksimowiczm.findmyip.settings.SettingClickableToggle
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme

@Composable
internal fun SaveAddressHistorySettings(
    onHistorySettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddressHistorySettingsViewModel = hiltViewModel()
) {
    val state by viewModel.saveHistoryState.collectAsStateWithLifecycle()

    SaveAddressHistorySettings(
        modifier = modifier,
        checked = state,
        onCheckedChange = {
            if (it) {
                viewModel.enableHistorySettings()
            } else {
                viewModel.disableHistorySettings()
            }
        },
        onAdvancedSettingsClick = onHistorySettingsClick
    )
}

@Composable
private fun SaveAddressHistorySettings(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onAdvancedSettingsClick: () -> Unit,
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

    SettingClickableToggle(
        headlineContent = { Text(stringResource(R.string.history_save)) },
        supportingContent = { Text(stringResource(R.string.history_save_description)) },
        checked = checked,
        onCheckedChange = {
            if (it) {
                showDialog = true
            } else {
                onCheckedChange(false)
            }
        },
        enabled = true,
        modifier = modifier,
        onClick = onAdvancedSettingsClick
    )
}

@PreviewLightDark
@Composable
private fun SaveAddressHistorySettingsPreview() {
    FindMyIpAppTheme {
        Surface {
            SaveAddressHistorySettings(
                checked = true,
                onCheckedChange = {},
                onAdvancedSettingsClick = {}
            )
        }
    }
}
