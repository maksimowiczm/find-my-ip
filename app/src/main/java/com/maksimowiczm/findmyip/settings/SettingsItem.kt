package com.maksimowiczm.findmyip.settings

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme

@Composable
fun SettingClickable(
    onClick: () -> Unit,
    headlineContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    supportingContent: (@Composable () -> Unit)? = null,
    highlight: Boolean = false
) {
    SettingInternal(
        highlight = highlight,
        onClick = onClick,
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        trailingContent = {},
        modifier = modifier.clickable(onClick = onClick)
    )
}

@Composable
fun SettingToggle(
    headlineContent: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    supportingContent: (@Composable () -> Unit)? = null,
    highlight: Boolean = false
) {
    SettingInternal(
        highlight = highlight,
        onClick = { onCheckedChange(!checked) },
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled
            )
        },
        modifier = modifier
    )
}

@Composable
fun SettingClickableToggle(
    headlineContent: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    supportingContent: (@Composable () -> Unit)? = null,
    highlight: Boolean = false
) {
    SettingInternal(
        highlight = highlight,
        onClick = onClick,
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VerticalDivider(
                    modifier = Modifier
                        .height(25.dp)
                        .padding(horizontal = 16.dp)
                )
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    enabled = enabled
                )
            }
        },
        modifier = modifier
    )
}

@Composable
private fun SettingInternal(
    onClick: () -> Unit,
    headlineContent: @Composable () -> Unit,
    trailingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    supportingContent: (@Composable () -> Unit)? = null,
    highlight: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "color")
    val animatedColor by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.surfaceContainer,
        targetValue = MaterialTheme.colorScheme.secondaryContainer,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color"
    )

    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        colors = if (highlight) {
            ListItemDefaults.colors(containerColor = animatedColor)
        } else {
            ListItemDefaults.colors()
        },
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        trailingContent = trailingContent
    )
}

@PreviewLightDark
@Composable
private fun PreviewSettingClickable() {
    FindMyIpAppTheme {
        Surface {
            SettingClickable(
                highlight = true,
                onClick = {},
                headlineContent = { Text("Setting") },
                supportingContent = { Text("Description") },
                modifier = Modifier
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewSettingToggle() {
    FindMyIpAppTheme {
        Surface {
            SettingToggle(
                highlight = false,
                headlineContent = { Text("Setting") },
                supportingContent = { Text("Description") },
                checked = true,
                onCheckedChange = {},
                enabled = true,
                modifier = Modifier
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewSettingClickableToggle() {
    FindMyIpAppTheme {
        Surface {
            SettingClickableToggle(
                highlight = false,
                headlineContent = { Text("Setting") },
                supportingContent = { Text("Description") },
                checked = true,
                onCheckedChange = {},
                enabled = true,
                onClick = {},
                modifier = Modifier
            )
        }
    }
}
