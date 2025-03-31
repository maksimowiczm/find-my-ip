@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.ui.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Suppress("ktlint:compose:content-slot-reused")
@Composable
fun ToggleSettingsScaffold(
    check: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean,
    topBar: @Composable () -> Unit,
    disabledContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = ToggleSettingsScaffoldDefaults.windowInsetsWithBottomBar,
    enabledContent: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        contentWindowInsets = contentWindowInsets
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            val headerContainerColor by animateColorAsState(
                targetValue = if (check) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceContainer
                }
            )

            val headerContentColor by animateColorAsState(
                targetValue = if (check) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )

            ListItem(
                modifier = Modifier
                    .then(
                        if (enabled) {
                            Modifier.clickable { onCheckedChange(!check) }
                        } else {
                            Modifier
                        }
                    ),
                colors = ListItemDefaults.colors(
                    containerColor = headerContainerColor,
                    headlineColor = headerContentColor
                ),
                headlineContent = {
                    if (check) {
                        Text(stringResource(Res.string.headline_on))
                    } else {
                        Text(stringResource(Res.string.headline_off))
                    }
                },
                trailingContent = {
                    Switch(
                        checked = check,
                        onCheckedChange = onCheckedChange,
                        enabled = enabled
                    )
                }
            )

            Crossfade(
                targetState = check
            ) {
                if (!it) {
                    disabledContent()
                } else {
                    enabledContent()
                }
            }
        }
    }
}

object ToggleSettingsScaffoldDefaults {
    val windowInsetsWithBottomBar: WindowInsets
        @Composable get() {
            val excludedPadding = WindowInsets.systemBars
                .union(WindowInsets.displayCutout)
                .union(WindowInsets.navigationBars)
                .only(WindowInsetsSides.Bottom)

            return WindowInsets.systemBars
                .union(WindowInsets.displayCutout)
                .union(WindowInsets.navigationBars)
                .exclude(excludedPadding)
        }
}
