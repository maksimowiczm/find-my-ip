package com.maksimowiczm.findmyip.feature.background.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.shared.core.feature.ui.ArrowBackIconButton
import com.maksimowiczm.findmyip.shared.core.feature.ui.FindMyIpTheme
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun RunInBackgroundScreen(
    onBack: () -> Unit,
    periodicRefreshRunning: Boolean,
    onTogglePeriodicRefresh: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            LargeFlexibleTopAppBar(
                title = { Text(stringResource(Res.string.headline_run_in_background)) },
                navigationIcon = { ArrowBackIconButton(onBack) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = paddingValues,
        ) {
            item {
                ListItem(
                    headlineContent = {
                        Text(stringResource(Res.string.headline_periodic_refresh))
                    },
                    modifier =
                        Modifier.clickable { onTogglePeriodicRefresh(!periodicRefreshRunning) },
                    supportingContent = {
                        Text(stringResource(Res.string.description_periodic_refresh))
                    },
                    leadingContent = {
                        Icon(painterResource(Res.drawable.ic_clock_loader_20), null)
                    },
                    trailingContent = {
                        Switch(checked = periodicRefreshRunning, onCheckedChange = null)
                    },
                )
            }
            item { Spacer(Modifier.height(16.dp)) }
            item {
                // TODO
                //  Enable when notifications are implemented
                return@item
                Surface(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.large,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(Icons.Outlined.Info, null)
                        Text(
                            text =
                                stringResource(
                                    Res.string.hint_combine_periodic_refresh_with_notifications
                                ),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun RunInBackgroundScreenPreview() {
    FindMyIpTheme {
        RunInBackgroundScreen(
            onBack = {},
            periodicRefreshRunning = true,
            onTogglePeriodicRefresh = {},
        )
    }
}
