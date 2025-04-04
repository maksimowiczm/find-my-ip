package com.maksimowiczm.findmyip.feature.settings.internetprotocol

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.maksimowiczm.findmyip.data.IpifyConfig
import findmyip.composeapp.generated.resources.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
fun InternetProtocolSettingsScreen(modifier: Modifier = Modifier) {
    InternetProtocolSettingsScreen(
        modifier = modifier,
        viewModel = koinViewModel()
    )
}

@Composable
internal fun InternetProtocolSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: InternetProtocolSettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    InternetProtocolSettingsScreen(
        state = state,
        eventBus = viewModel.eventBus,
        onIpv4Toggle = remember(viewModel) {
            { enabled: Boolean -> viewModel.toggleIPv4(enabled) }
        },
        onIpv6Toggle = remember(viewModel) {
            { enabled: Boolean -> viewModel.toggleIPv6(enabled) }
        },
        onTest = remember(viewModel) { viewModel::testInternetProtocols },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InternetProtocolSettingsScreen(
    state: InternetProtocolSettingsState,
    eventBus: Flow<InternetProtocolSettingsEvent>,
    onIpv4Toggle: (Boolean) -> Unit,
    onIpv6Toggle: (Boolean) -> Unit,
    onTest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var testIsRunning by rememberSaveable { mutableStateOf(false) }

    val testFinishedString = stringResource(Res.string.description_test_finished_internet_protocols)
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(eventBus) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            eventBus.collect {
                when (it) {
                    InternetProtocolSettingsEvent.StartTest -> {
                        testIsRunning = true
                    }

                    InternetProtocolSettingsEvent.StopTest -> {
                        delay(200)
                        testIsRunning = false
                        launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(testFinishedString)
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.headline_internet_protocol),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues
        ) {
            stickyHeader {
                Box(Modifier.height(4.dp)) {
                    updateTransition(testIsRunning).AnimatedVisibility(
                        visible = { it },
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        LinearProgressIndicator(Modifier.fillMaxWidth())
                    }
                }
            }

            item {
                Text(
                    text = stringResource(Res.string.description_internet_protocol),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                ListItem(
                    headlineContent = { Text(stringResource(Res.string.ipv4)) },
                    modifier = Modifier.clickable { onIpv4Toggle(!state.ipv4) },
                    supportingContent = { Text(IpifyConfig.IPV4) },
                    trailingContent = {
                        Switch(
                            checked = state.ipv4,
                            enabled = state.canSwitchIpv4,
                            onCheckedChange = onIpv4Toggle
                        )
                    }
                )
            }

            item {
                ListItem(
                    headlineContent = { Text(stringResource(Res.string.ipv6)) },
                    modifier = Modifier.clickable { onIpv6Toggle(!state.ipv6) },
                    supportingContent = { Text(IpifyConfig.IPV6) },
                    trailingContent = {
                        Switch(
                            checked = state.ipv6,
                            enabled = state.canSwitchIpv6,
                            onCheckedChange = onIpv6Toggle
                        )
                    }
                )
            }

            item {
                val headlineColor = if (testIsRunning) {
                    MaterialTheme.colorScheme.outline
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
                val supportingColor = if (testIsRunning) {
                    MaterialTheme.colorScheme.outline
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }

                ListItem(
                    headlineContent = {
                        Text(stringResource(Res.string.action_test_internet_protocols))
                    },
                    modifier = Modifier.clickable(
                        enabled = !testIsRunning,
                        onClick = { onTest() }
                    ),
                    supportingContent = {
                        Text(stringResource(Res.string.description_test_internet_protocols))
                    },
                    colors = ListItemDefaults.colors(
                        headlineColor = headlineColor,
                        supportingColor = supportingColor
                    )
                )
            }
        }
    }
}
