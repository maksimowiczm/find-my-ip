package com.maksimowiczm.findmyip.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState

@Composable
fun SwitchSettingListItem(
    headlineContent: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = modifier
    ) {
        ListItem(
            headlineContent = headlineContent,
            modifier = Modifier
                .testTag(SwitchSettingListItemTestTags.SURFACE)
                .clickable { onCheckedChange(!checked) }
                .semantics {
                    role = Role.Switch
                    toggleableState = if (checked) {
                        ToggleableState.On
                    } else {
                        ToggleableState.Off
                    }
                },
            supportingContent = supportingContent,
            leadingContent = leadingContent,
            trailingContent = {
                Switch(
                    checked = checked,
                    onCheckedChange = null
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
    }
}

object SwitchSettingListItemTestTags {
    const val SURFACE = "switch_setting_list_item_surface"
}
