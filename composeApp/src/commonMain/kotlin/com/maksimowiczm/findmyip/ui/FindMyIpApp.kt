package com.maksimowiczm.findmyip.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.maksimowiczm.findmyip.navigation.FindMyIpNavHost
import com.maksimowiczm.findmyip.navigation.TopRoute
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.action_dont_show_again
import findmyip.composeapp.generated.resources.action_hide
import findmyip.composeapp.generated.resources.description_migration_from_version_1
import findmyip.composeapp.generated.resources.headline_migration_from_version_1
import findmyip.composeapp.generated.resources.history
import findmyip.composeapp.generated.resources.home
import findmyip.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
fun FindMyIpApp(modifier: Modifier = Modifier, viewModel: MigrationViewModel = koinViewModel()) {
    val navController = rememberNavController()
    val appState = rememberFindMyIpAppState(navController)
    val selectedTopRoute = appState.currentTopRoute

    val showMigrationDialog by viewModel.showMigrationDialog.collectAsStateWithLifecycle()

    if (showMigrationDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(stringResource(Res.string.headline_migration_from_version_1))
            },
            text = {
                Text(stringResource(Res.string.description_migration_from_version_1))
            },
            dismissButton = {
                TextButton(
                    onClick = viewModel::hideMigrationDialog
                ) {
                    Text(stringResource(Res.string.action_hide))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = viewModel::dontShowMigrationDialogAgain
                ) {
                    Text(stringResource(Res.string.action_dont_show_again))
                }
            }
        )
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(Res.string.home)) },
                selected = selectedTopRoute == TopRoute.Home,
                onClick = { appState.navigate(TopRoute.Home) }
            )
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(Res.string.history)) },
                selected = selectedTopRoute == TopRoute.History,
                onClick = { appState.navigate(TopRoute.History) }
            )
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(Res.string.settings)) },
                selected = selectedTopRoute == TopRoute.Settings,
                onClick = { appState.navigate(TopRoute.Settings) }
            )
        }
    ) {
        Scaffold(
            modifier = modifier,
            contentWindowInsets = WindowInsets(0.dp)
        ) { paddingValues ->
            FindMyIpNavHost(
                navController = navController,
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
            )
        }
    }
}
