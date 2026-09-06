package com.maksimowiczm.findmyip.shared.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.shared.feature.home.persentation.Filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeTopBar(
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
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
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
            modifier = Modifier.sizeIn(minHeight = 56.dp, maxWidth = 720.dp).weight(1f, false),
        )
        TopBarIconButton(
            onClick = onSettings,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
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
    IconButton(
        shapes = IconButtonDefaults.shapes(),
        onClick = onClick,
        modifier = modifier,
        colors =
            IconButtonDefaults.iconButtonColors(
                containerColor = containerColor,
                contentColor = contentColor,
            ),
    ) {
        content()
    }
}
