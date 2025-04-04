package com.maksimowiczm.findmyip.feature.settings.backgroundservice

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.infrastructure.di.container
import com.valentinilk.shimmer.shimmer
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import pro.respawn.flowmvi.api.IntentReceiver
import pro.respawn.flowmvi.compose.dsl.subscribe

@Composable
actual fun BackgroundServiceSettingsScreen(modifier: Modifier) {
    BackgroundServiceSettingsScreen(
        modifier = modifier,
        container = container()
    )
}

@Composable
private fun BackgroundServiceSettingsScreen(
    modifier: Modifier = Modifier,
    container: BackgroundServiceSettingsContainer = container()
) = with(container.store) {
    val state by subscribe()

    BackgroundServiceSettingsScreen(
        state = state,
        modifier = modifier
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun IntentReceiver<BackgroundServiceSettingsIntent>.BackgroundServiceSettingsScreen(
    state: BackgroundServiceSettingsState,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val interactionEnabled =
        state !is BackgroundServiceSettingsState.Enabling &&
            state !is BackgroundServiceSettingsState.Disabling

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.headline_background_service),
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            item {
                Card(
                    onClick = {
                        when (state) {
                            BackgroundServiceSettingsState.Enabled ->
                                intent(BackgroundServiceSettingsIntent.Disable)

                            BackgroundServiceSettingsState.Disabled ->
                                intent(BackgroundServiceSettingsIntent.Enable)

                            else -> Unit
                        }
                    },
                    enabled = interactionEnabled,
                    modifier = Modifier.padding(16.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = stringResource(Res.string.action_use_background_service),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = if (interactionEnabled) {
                                    Modifier
                                } else {
                                    Modifier.shimmer()
                                }
                            )
                        },
                        modifier = Modifier.padding(8.dp),
                        trailingContent = {
                            Switch(
                                checked = state is BackgroundServiceSettingsState.Enabled ||
                                    state is BackgroundServiceSettingsState.Enabling,
                                enabled = interactionEnabled,
                                onCheckedChange = {
                                    when (it) {
                                        true -> intent(BackgroundServiceSettingsIntent.Enable)
                                        false -> intent(BackgroundServiceSettingsIntent.Disable)
                                    }
                                }
                            )
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        )
                    )
                }
            }

            item {
                Text(
                    text = stringResource(Res.string.description_background_worker),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

//            item {
//                ListItem(
//                    headlineContent = {
//                        Text(stringResource(Res.string.description_background_service_notification))
//                    },
//                    modifier = Modifier.clickable { /* TODO */ },
//                    leadingContent = {
//                        Icon(
//                            imageVector = Icons.Default.MailOutline,
//                            contentDescription = null
//                        )
//                    },
//                    supportingContent = {
//                        Text(
//                            text = stringResource(
//                                Res.string.description_background_service_notification
//                            )
//                        )
//                    },
//                    trailingContent = {
//                        Switch(
//                            checked = false,
//                            onCheckedChange = {}
//                        )
//                    }
//                )
//            }
        }
    }
}
