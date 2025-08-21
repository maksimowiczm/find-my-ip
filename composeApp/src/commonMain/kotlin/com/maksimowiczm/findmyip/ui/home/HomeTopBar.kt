package com.maksimowiczm.findmyip.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.presentation.home.Filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    filter: Filter,
    searchTextState: TextFieldState,
    onSearch: (String) -> Unit,
    onVolunteer: () -> Unit,
    onSettings: () -> Unit,
    onFilter: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .windowInsetsPadding(SearchBarDefaults.windowInsets)
                .consumeWindowInsets(SearchBarDefaults.windowInsets),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TopBarIconButton(
            onClick = onVolunteer,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        ) {
            Icon(Icons.Outlined.VolunteerActivism, null)
        }
        HomeSearchBar(
            filtersCount = filter.filtersCount,
            state = searchTextState,
            onSearch = onSearch,
            onFilter = onFilter,
            modifier = Modifier.weight(1f),
        )
        TopBarIconButton(
            onClick = onSettings,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            Icon(Icons.Filled.Settings, null)
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TopBarIconButton(
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val shape = CircleShape

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        shadowElevation = 4.dp,
    ) {
        Box(
            modifier =
                modifier
                    .size(IconButtonDefaults.smallContainerSize())
                    .clickable(
                        onClick = onClick,
                        role = Role.Button,
                        interactionSource = null,
                        indication = ripple(),
                    ),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}
