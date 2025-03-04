package com.maksimowiczm.findmyip.ui.settings

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.ui.settings.history.HistorySettings
import com.maksimowiczm.findmyip.ui.settings.internetprotocol.InternetProtocolVersionSettings
import findmyip.composeapp.generated.resources.*
import findmyip.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onAdvancedHistorySettingsClick: () -> Unit,
    platformSettings: LazyListScope.() -> Unit,
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

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(Res.string.headline_settings))
                },
                scrollBehavior = scrollBehavior
            )
        },
        contentWindowInsets = contentPadding
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = paddingValues
        ) {
            item {
                HistorySettings(
                    onAdvancedSettingsClick = onAdvancedHistorySettingsClick
                )
            }

            item {
                HorizontalDivider()
            }

            item {
                InternetProtocolVersionSettings()
            }

            item {
                HorizontalDivider()
            }

            item {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    text = stringResource(Res.string.headline_platform_settings),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            platformSettings()
        }
    }
}
