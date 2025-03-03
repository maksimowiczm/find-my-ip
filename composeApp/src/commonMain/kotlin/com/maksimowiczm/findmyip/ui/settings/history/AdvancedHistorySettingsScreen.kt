package com.maksimowiczm.findmyip.ui.settings.history

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.back
import findmyip.composeapp.generated.resources.headline_history_settings
import findmyip.composeapp.generated.resources.history_disabled_description
import findmyip.composeapp.generated.resources.off
import findmyip.composeapp.generated.resources.on
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdvancedHistorySettingsScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistorySettingsViewModel = koinViewModel()
) {
    val isEnabled by viewModel.isEnabled.collectAsStateWithLifecycle()

    AdvancedHistorySettingsScreen(
        onNavigateUp = onNavigateUp,
        enabled = isEnabled,
        onEnabledChange = viewModel::onEnableChange,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdvancedHistorySettingsScreen(
    onNavigateUp: () -> Unit,
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val excludedPadding = WindowInsets.systemBars
        .union(WindowInsets.displayCutout)
        .union(WindowInsets.navigationBars)
        .only(WindowInsetsSides.Bottom)

    val contentPadding = WindowInsets.systemBars
        .union(WindowInsets.displayCutout)
        .union(WindowInsets.navigationBars)
        .exclude(excludedPadding)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(Res.string.headline_history_settings))
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateUp
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                }
            )
        },
        contentWindowInsets = contentPadding
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            val headerColor by animateColorAsState(
                targetValue = if (enabled) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceContainer
                }
            )

            ListItem(
                modifier = Modifier.clickable { onEnabledChange(!enabled) },
                colors = ListItemDefaults.colors(
                    containerColor = headerColor
                ),
                headlineContent = {
                    if (enabled) {
                        Text(stringResource(Res.string.on))
                    } else {
                        Text(stringResource(Res.string.off))
                    }
                },
                trailingContent = {
                    Switch(
                        checked = enabled,
                        onCheckedChange = onEnabledChange
                    )
                }
            )

            Spacer(Modifier.height(8.dp))

            Crossfade(
                targetState = enabled
            ) {
                if (!it) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(Res.string.history_disabled_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                } else {
                    EnabledContent()
                }
            }
        }
    }
}

@Composable
private fun EnabledContent(modifier: Modifier = Modifier) {
}
