package com.maksimowiczm.findmyip.feature.settings.backgroundservice

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.action_use_background_service
import findmyip.composeapp.generated.resources.description_background_service_notification
import findmyip.composeapp.generated.resources.description_background_worker
import findmyip.composeapp.generated.resources.headline_background_service
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun BackgroundServiceSettingsScreen(modifier: Modifier) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
                    onClick = {},
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
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        modifier = Modifier.padding(8.dp),
                        trailingContent = {
                            Switch(
                                checked = false,
                                onCheckedChange = {}
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

            item {
                ListItem(
                    headlineContent = {
                        Text(stringResource(Res.string.description_background_service_notification))
                    },
                    modifier = Modifier.clickable { /* TODO */ },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.MailOutline,
                            contentDescription = null
                        )
                    },
                    supportingContent = {
                        Text(
                            text = stringResource(
                                Res.string.description_background_service_notification
                            )
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = false,
                            onCheckedChange = {}
                        )
                    }
                )
            }
        }
    }
}
