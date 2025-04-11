package com.maksimowiczm.findmyip.feature.settings.global

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.SettingsEthernet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import findmyip.composeapp.generated.resources.*
import findmyip.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onHistorySettings: () -> Unit,
    onInternetProtocolSettings: () -> Unit,
    onLanguageSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.headline_settings),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues
        ) {
            item {
                ListItem(
                    headlineContent = { Text(stringResource(Res.string.headline_history)) },
                    modifier = Modifier
                        .clickable { onHistorySettings() }
                        .padding(horizontal = 8.dp),
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null
                        )
                    },
                    supportingContent = {
                        Text(stringResource(Res.string.description_save_history))
                    }
                )
            }

            item {
                ListItem(
                    headlineContent = {
                        Text(stringResource(Res.string.headline_internet_protocol))
                    },
                    modifier = Modifier
                        .clickable { onInternetProtocolSettings() }
                        .padding(horizontal = 8.dp),
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.SettingsEthernet,
                            contentDescription = null
                        )
                    },
                    supportingContent = {
                        val text = buildString {
                            append(stringResource(Res.string.ipv4))
                            append(" / ")
                            append(stringResource(Res.string.ipv6))
                        }

                        Text(text)
                    }
                )
            }

            item {
                ListItem(
                    headlineContent = { Text(stringResource(Res.string.headline_language)) },
                    modifier = Modifier
                        .clickable { onLanguageSettings() }
                        .padding(horizontal = 8.dp),
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}
