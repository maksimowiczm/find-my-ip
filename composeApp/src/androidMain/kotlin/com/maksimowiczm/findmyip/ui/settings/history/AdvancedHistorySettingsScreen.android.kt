package com.maksimowiczm.findmyip.ui.settings.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
actual fun AdvancedHistoryPlatformSettings(modifier: Modifier) {
    BackgroundWorkerSettingsImpl(modifier = modifier)
}

@Composable
private fun BackgroundWorkerSettingsImpl(
    modifier: Modifier = Modifier,
    viewModel: BackgroundWorkerViewModel = koinViewModel()
) {
    val isEnabled by viewModel.workerStatus.collectAsStateWithLifecycle()

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

    val dozeLink = stringResource(Res.string.link_doze)
    val dozeLearn = stringResource(Res.string.action_learn_about_doze)
    val linkColor = MaterialTheme.colorScheme.primary
    val dozeLinkString = remember(dozeLink) {
        buildAnnotatedString {
            withLink(
                link = LinkAnnotation.Url(
                    url = dozeLink,
                    styles = TextLinkStyles(SpanStyle(linkColor))
                )
            ) {
                append(dozeLearn)
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(Res.string.headline_background_worker),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(Res.string.description_background_worker),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(8.dp))

        ListItem(
            headlineContent = {
                Text(stringResource(Res.string.action_enable))
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
                stringResource(Res.string.headline_run_every),
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

        Card(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(Res.string.headline_doze),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(Res.string.description_doze),
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = dozeLinkString,
                    style = MaterialTheme.typography.bodyMedium
                )
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
