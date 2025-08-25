package com.maksimowiczm.findmyip.shared.feature.home.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeSearchBar(
    filtersCount: Int,
    state: TextFieldState,
    onSearch: (String) -> Unit,
    onFilter: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = CircleShape,
        shadowElevation = 4.dp,
    ) {
        SearchBarDefaults.InputField(
            state = state,
            onSearch = onSearch,
            expanded = false,
            onExpandedChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(Res.string.action_search)) },
            leadingIcon = { Icon(Icons.Outlined.Search, null) },
            trailingIcon = {
                IconButton(onFilter) {
                    BadgedBox(
                        badge = {
                            if (filtersCount > 0) {
                                Badge { Text(filtersCount.toString()) }
                            }
                        },
                        content = { Icon(Icons.Outlined.FilterAlt, null) },
                    )
                }
            },
        )
    }
}
