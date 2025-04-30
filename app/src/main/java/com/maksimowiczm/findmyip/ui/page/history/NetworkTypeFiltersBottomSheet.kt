package com.maksimowiczm.findmyip.ui.page.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.NetworkWifi
import androidx.compose.material.icons.outlined.VpnKey
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.domain.model.NetworkType
import com.maksimowiczm.findmyip.ui.theme.FindMyIPTheme

@Composable
fun rememberNetworkTypeFiltersState(filters: List<NetworkType>) = rememberSaveable(
    filters,
    saver = Saver(
        save = {
            it.filters.map {
                when (it) {
                    NetworkType.WiFi -> 0
                    NetworkType.Cellular -> 1
                    NetworkType.VPN -> 2
                }
            }
        },
        restore = {
            val filters = it.map { index ->
                when (index) {
                    0 -> NetworkType.WiFi
                    1 -> NetworkType.Cellular
                    2 -> NetworkType.VPN
                    else -> error("Unknown filter")
                }
            }

            NetworkTypeFiltersState(
                initialFilters = filters
            )
        }
    )
) {
    NetworkTypeFiltersState(filters)
}

class NetworkTypeFiltersState(initialFilters: List<NetworkType>) {
    var filters: List<NetworkType> by mutableStateOf(initialFilters)
        private set

    val wifi: Boolean
        get() = filters.any { it == NetworkType.WiFi }

    val cellular: Boolean
        get() = filters.any { it == NetworkType.Cellular }

    val vpn: Boolean
        get() = filters.any { it == NetworkType.VPN }

    fun toggleFilter(filter: NetworkType) {
        filters = if (filters.contains(filter)) {
            filters - filter
        } else {
            filters + filter
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkTypeFiltersBottomSheet(
    state: NetworkTypeFiltersState,
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
        NetworkTypeFiltersBottomSheetContent(
            state = state,
            onDismissRequest = onDismissRequest,
            onConfirm = onConfirm
        )
    }
}

@Composable
private fun NetworkTypeFiltersBottomSheetContent(
    state: NetworkTypeFiltersState,
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
                    text = stringResource(R.string.network_type),
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
                Text(stringResource(R.string.wifi))
            },
            modifier = Modifier.clickable { state.toggleFilter(NetworkType.WiFi) },
            leadingContent = {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NetworkWifi,
                        contentDescription = null
                    )
                }
            },
            trailingContent = {
                Checkbox(
                    checked = state.wifi,
                    onCheckedChange = { state.toggleFilter(NetworkType.WiFi) }
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
        HorizontalDivider(Modifier.padding(horizontal = 16.dp))
        ListItem(
            headlineContent = {
                Text(stringResource(R.string.cellular))
            },
            modifier = Modifier.clickable { state.toggleFilter(NetworkType.Cellular) },
            leadingContent = {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NetworkCell,
                        contentDescription = null
                    )
                }
            },
            trailingContent = {
                Checkbox(
                    checked = state.cellular,
                    onCheckedChange = { state.toggleFilter(NetworkType.Cellular) }
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
        HorizontalDivider(Modifier.padding(horizontal = 16.dp))
        ListItem(
            headlineContent = {
                Text(stringResource(R.string.vpn))
            },
            modifier = Modifier.clickable { state.toggleFilter(NetworkType.VPN) },
            leadingContent = {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.VpnKey,
                        contentDescription = null
                    )
                }
            },
            trailingContent = {
                Checkbox(
                    checked = state.vpn,
                    onCheckedChange = { state.toggleFilter(NetworkType.VPN) }
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true
)
@Composable
private fun NetworkTypeFiltersBottomSheetPreview() {
    FindMyIPTheme {
        NetworkTypeFiltersBottomSheetContent(
            state = rememberNetworkTypeFiltersState(emptyList()),
            onDismissRequest = {},
            onConfirm = {}
        )
    }
}
