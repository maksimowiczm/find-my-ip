package com.maksimowiczm.findmyip.ui.page.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.ui.theme.FindMyIPTheme

@Composable
fun rememberInternetProtocolsFiltersState(filters: List<InternetProtocol>) = rememberSaveable(
    filters,
    saver = Saver(
        save = {
            it.filters.map {
                when (it) {
                    InternetProtocol.IPv4 -> 0
                    InternetProtocol.IPv6 -> 1
                }
            }
        },
        restore = {
            val filters = it.map { index ->
                when (index) {
                    0 -> InternetProtocol.IPv4
                    1 -> InternetProtocol.IPv6
                    else -> error("Unknown filter")
                }
            }

            InternetProtocolsFiltersState(
                initialFilters = filters
            )
        }
    )
) {
    InternetProtocolsFiltersState(
        initialFilters = filters
    )
}

class InternetProtocolsFiltersState(initialFilters: List<InternetProtocol>) {
    val ipv4: Boolean
        get() = filters.any { it == InternetProtocol.IPv4 }

    val ipv6: Boolean
        get() = filters.any { it == InternetProtocol.IPv6 }

    var filters: List<InternetProtocol> by mutableStateOf(initialFilters)
        private set

    fun toggleFilter(filter: InternetProtocol) {
        filters = if (filters.contains(filter)) {
            filters - filter
        } else {
            filters + filter
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternetProtocolsFiltersBottomSheet(
    state: InternetProtocolsFiltersState,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        dragHandle = {}
    ) {
        InternetProtocolsFiltersBottomSheetContent(
            state = state,
            onDismissRequest = onDismissRequest,
            onConfirm = onConfirm
        )
    }
}

@Composable
private fun InternetProtocolsFiltersBottomSheetContent(
    state: InternetProtocolsFiltersState,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.headline_internet_protocols),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            leadingContent = {
                IconButton(
                    onClick = onDismissRequest
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.action_close)
                    )
                }
            },
            trailingContent = {
                IconButton(
                    onClick = onConfirm
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.action_confirm)
                    )
                }
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
        ListItem(
            headlineContent = {
                Text(stringResource(R.string.ipv4))
            },
            modifier = Modifier.clickable { state.toggleFilter(InternetProtocol.IPv4) },
            trailingContent = {
                Checkbox(
                    checked = state.ipv4,
                    onCheckedChange = { state.toggleFilter(InternetProtocol.IPv4) }
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
        HorizontalDivider(Modifier.padding(horizontal = 16.dp))
        ListItem(
            headlineContent = {
                Text(stringResource(R.string.ipv6))
            },
            modifier = Modifier.clickable { state.toggleFilter(InternetProtocol.IPv6) },
            trailingContent = {
                Checkbox(
                    checked = state.ipv6,
                    onCheckedChange = { state.toggleFilter(InternetProtocol.IPv6) }
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun InternetProtocolsFiltersBottomSheetPreview() {
    FindMyIPTheme {
        InternetProtocolsFiltersBottomSheetContent(
            state = rememberInternetProtocolsFiltersState(
                filters = listOf(InternetProtocol.IPv4)
            ),
            onDismissRequest = {},
            onConfirm = {}
        )
    }
}
