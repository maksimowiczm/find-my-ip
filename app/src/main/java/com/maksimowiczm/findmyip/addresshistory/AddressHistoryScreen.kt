package com.maksimowiczm.findmyip.addresshistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.domain.AddressHistory
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme
import kotlinx.coroutines.launch

@Composable
fun AddressHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: AddressHistoryViewModel = hiltViewModel()
) {
    val hasPermission by viewModel.hasPermission.collectAsStateWithLifecycle()
    val ipv4HistoryState by viewModel.ipv4HistoryState.collectAsStateWithLifecycle()
    val ipv6HistoryState by viewModel.ipv6HistoryState.collectAsStateWithLifecycle()

    AddressHistoryScreen(
        modifier = modifier,
        hasPermission = hasPermission,
        onGrantPermission = viewModel::onGrantPermission,
        ipv4HistoryState = ipv4HistoryState,
        ipv6HistoryState = ipv6HistoryState
    )
}

@Composable
private fun AddressHistoryScreen(
    hasPermission: Boolean,
    onGrantPermission: () -> Unit,
    ipv4HistoryState: AddressHistoryState,
    ipv6HistoryState: AddressHistoryState,
    modifier: Modifier = Modifier
) {
    if (ipv4HistoryState is AddressHistoryState.Loading ||
        ipv6HistoryState is AddressHistoryState.Loading
    ) {
        return Box(modifier) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }

    if (!hasPermission) {
        return NoPermissionScreen(
            modifier = modifier,
            onGrantPermission = onGrantPermission
        )
    }

    if (
        ipv4HistoryState is AddressHistoryState.Loaded &&
        ipv6HistoryState is AddressHistoryState.Disabled
    ) {
        return AddressHistoryList(
            modifier = modifier,
            items = ipv4HistoryState.addressHistory
        )
    }

    if (
        ipv4HistoryState is AddressHistoryState.Disabled &&
        ipv6HistoryState is AddressHistoryState.Loaded
    ) {
        return AddressHistoryList(
            modifier = modifier,
            items = ipv6HistoryState.addressHistory
        )
    }

    if (
        ipv4HistoryState is AddressHistoryState.Loaded &&
        ipv6HistoryState is AddressHistoryState.Loaded
    ) {
        return TabAddressHistoryList(
            modifier = modifier,
            ipv4History = ipv4HistoryState.addressHistory,
            ipv6History = ipv6HistoryState.addressHistory
        )
    }
}

@Composable
private fun NoPermissionScreen(onGrantPermission: () -> Unit, modifier: Modifier = Modifier) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        HistoryPermissionDialog(
            onDismiss = { showDialog = false },
            onGrantPermission = onGrantPermission
        )
    }

    Column(
        modifier = modifier.clickable { showDialog = true },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.history_no_permission_description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.tap_to_enable),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun TabAddressHistoryList(
    ipv4History: List<AddressHistory>,
    ipv6History: List<AddressHistory>,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        val pagerState = rememberPagerState(pageCount = { 2 })
        val coroutineScope = rememberCoroutineScope()

        TabRow(
            selectedTabIndex = pagerState.currentPage

        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(0) } }
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(R.string.ipv4)
                )
            }
            Tab(
                selected = pagerState.currentPage == 1,
                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) } }
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(R.string.ipv6)
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) { page ->
            when (page) {
                0 -> AddressHistoryList(
                    modifier = Modifier.fillMaxSize(),
                    items = ipv4History
                )

                1 -> AddressHistoryList(
                    modifier = Modifier.fillMaxSize(),
                    items = ipv6History
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewAddressHistoryScreen() {
    FindMyIpAppTheme {
        Surface {
            AddressHistoryScreen(
                hasPermission = true,
                onGrantPermission = {},
                ipv4HistoryState = AddressHistoryState.Loaded(
                    listOf(
                        AddressHistory(
                            ip = "127.0.0.1",
                            date = "January 1, 2024"
                        ),
                        AddressHistory(
                            ip = "127.0.0.1",
                            date = "January 2, 2024"
                        )
                    )
                ),
                ipv6HistoryState = AddressHistoryState.Loaded(
                    listOf(
                        AddressHistory(
                            ip = "::1",
                            date = "January 1, 2024"
                        ),
                        AddressHistory(
                            ip = "::1",
                            date = "January 2, 2024"
                        )
                    )
                )
            )
        }
    }
}
