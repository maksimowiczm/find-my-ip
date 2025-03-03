package com.maksimowiczm.findmyip.ui.settings.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.action_cancel
import findmyip.composeapp.generated.resources.action_confirm
import findmyip.composeapp.generated.resources.action_learn_about_doze
import findmyip.composeapp.generated.resources.description_background_worker
import findmyip.composeapp.generated.resources.description_interval_less_than_15_min
import findmyip.composeapp.generated.resources.enable
import findmyip.composeapp.generated.resources.headline_background_worker
import findmyip.composeapp.generated.resources.headline_check_every
import findmyip.composeapp.generated.resources.headline_custom
import findmyip.composeapp.generated.resources.headline_custom_interval
import findmyip.composeapp.generated.resources.link_doze
import findmyip.composeapp.generated.resources.unit_minutes_short
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
actual fun BackgroundWorkerSettings(modifier: Modifier) {
    BackgroundWorkerSettingsImpl(
        modifier = modifier
    )
}

@Composable
private fun BackgroundWorkerSettingsImpl(
    modifier: Modifier = Modifier,
    viewModel: BackgroundWorkerViewModel = koinViewModel()
) {
    val isEnabled by viewModel.workerStatus.collectAsStateWithLifecycle(false)

    val interval by viewModel.interval.collectAsStateWithLifecycle()

    BackgroundWorkerSettingsImpl(
        enabled = isEnabled,
        setEnabled = viewModel::setEnabled,
        selectedInterval = interval,
        onInterval = viewModel::setRefreshInterval,
        modifier = modifier
    )
}

@Composable
private fun BackgroundWorkerSettingsImpl(
    enabled: Boolean,
    setEnabled: (Boolean) -> Unit,
    selectedInterval: Long,
    onInterval: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val intervals = arrayOf<Long>(60, 240, 720, 1440)

    var showCustomDialog by rememberSaveable { mutableStateOf(false) }

    if (showCustomDialog) {
        CustomIntervalDialog(
            onDismissRequest = { showCustomDialog = false },
            onConfirm = {
                onInterval(it)
                showCustomDialog = false
            }
        )
    }

    val description = stringResource(Res.string.description_background_worker)
    val link = stringResource(Res.string.link_doze)
    val linkColor = MaterialTheme.colorScheme.primary
    val learn = stringResource(Res.string.action_learn_about_doze)
    val annotatedLinkString = remember {
        buildAnnotatedString {
            append(description)
            append(" ")
            withLink(LinkAnnotation.Url(link, TextLinkStyles(SpanStyle(linkColor)))) {
                append(learn)
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            text = stringResource(Res.string.headline_background_worker),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = annotatedLinkString,
            style = MaterialTheme.typography.bodyMedium
        )
        ListItem(
            headlineContent = {
                Text(stringResource(Res.string.enable))
            },
            modifier = Modifier.clickable { setEnabled(!enabled) },
            trailingContent = {
                Switch(
                    checked = enabled,
                    onCheckedChange = setEnabled
                )
            }
        )
        if (enabled) {
            Text(
                stringResource(Res.string.headline_check_every),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    SuggestionChip(
                        onClick = { showCustomDialog = true },
                        label = { Text(stringResource(Res.string.headline_custom)) }
                    )
                }
                if (!intervals.contains(selectedInterval)) {
                    item {
                        FilterChip(
                            selected = true,
                            onClick = { onInterval(selectedInterval) },
                            label = {
                                Text(
                                    "$selectedInterval " +
                                        stringResource(Res.string.unit_minutes_short)
                                )
                            }
                        )
                    }
                }

                items(intervals) { interval ->
                    FilterChip(
                        selected = interval == selectedInterval,
                        onClick = { onInterval(interval) },
                        label = {
                            Text(
                                "$interval " + stringResource(Res.string.unit_minutes_short)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomIntervalDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var interval by rememberSaveable { mutableStateOf("") }
    val asLong by remember(interval) {
        derivedStateOf { interval.toLongOrNull() }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                enabled = asLong != null,
                onClick = {
                    if (asLong != null) {
                        onConfirm(asLong!!)
                    }
                }
            ) {
                Text(stringResource(Res.string.action_confirm))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(stringResource(Res.string.action_cancel))
            }
        },
        title = {
            Text(stringResource(Res.string.headline_custom_interval))
        },
        text = {
            Column {
                Text(
                    text = stringResource(Res.string.description_interval_less_than_15_min)
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = interval,
                    onValueChange = { interval = it },
                    isError = interval.isNotEmpty() && asLong == null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Go
                    ),
                    suffix = { Text(stringResource(Res.string.unit_minutes_short)) },
                    keyboardActions = KeyboardActions {
                        if (asLong != null) {
                            onConfirm(asLong!!)
                        }
                    }
                )
            }
        }
    )
}

@Preview(
    showBackground = true
)
@Composable
private fun BackgroundWorkerSettingsPreview() {
    FindMyIpAppTheme {
        BackgroundWorkerSettingsImpl(
            enabled = true,
            setEnabled = {},
            selectedInterval = 120,
            onInterval = {},
            modifier = Modifier
        )
    }
}

@Preview
@Composable
private fun CustomIntervalDialogPreview() {
    FindMyIpAppTheme {
        CustomIntervalDialog(
            onDismissRequest = {},
            onConfirm = {}
        )
    }
}
