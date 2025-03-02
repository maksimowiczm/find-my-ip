package com.maksimowiczm.findmyip.old.feature.settings.addresshistory

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.old.feature.addresshistory.HistoryPermissionDialog
import com.maksimowiczm.findmyip.old.feature.settings.SettingClickableToggle
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.history_save
import findmyip.composeapp.generated.resources.history_save_description
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun SaveAddressHistorySettings(
    onHistorySettingsClick: () -> Unit,
    highlight: Boolean,
    modifier: Modifier = Modifier,
    viewModel: AddressHistorySettingsViewModel = koinViewModel()
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
        onAdvancedSettingsClick = onHistorySettingsClick,
        highlight = highlight
    )
}

@Composable
private fun SaveAddressHistorySettings(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onAdvancedSettingsClick: () -> Unit,
    highlight: Boolean,
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
        modifier = modifier,
        headlineContent = { Text(stringResource(Res.string.history_save)) },
        supportingContent = { Text(stringResource(Res.string.history_save_description)) },
        checked = checked,
        onCheckedChange = {
            if (it) {
                showDialog = true
            } else {
                onCheckedChange(false)
            }
        },
        enabled = true,
        onClick = onAdvancedSettingsClick,
        highlight = highlight
    )
}
