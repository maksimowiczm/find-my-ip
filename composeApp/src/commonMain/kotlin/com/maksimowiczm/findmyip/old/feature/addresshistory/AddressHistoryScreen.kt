package com.maksimowiczm.findmyip.old.feature.addresshistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.old.domain.AddressHistory
import com.maksimowiczm.findmyip.old.feature.addresshistory.AddressHistoryState.Disabled
import com.maksimowiczm.findmyip.old.feature.addresshistory.AddressHistoryState.Loaded
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.history_disabled_card_description
import findmyip.composeapp.generated.resources.history_no_permission_description
import findmyip.composeapp.generated.resources.ipv4
import findmyip.composeapp.generated.resources.ipv6
import findmyip.composeapp.generated.resources.tap_to_enable
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddressHistoryScreen(
    onGrantPermission: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddressHistoryViewModel = koinViewModel()
) {
    val hasPermission by viewModel.hasPermission.collectAsStateWithLifecycle()
    val ipv4HistoryState by viewModel.ipv4HistoryState.collectAsStateWithLifecycle()
    val ipv6HistoryState by viewModel.ipv6HistoryState.collectAsStateWithLifecycle()

    AddressHistoryScreen(
        modifier = modifier,
        hasPermission = hasPermission,
        onGrantPermission = onGrantPermission,
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

    val shouldShowNoPermission = !hasPermission &&
        (
            // Linter hack 💀
            false ||
                ipv4HistoryState is Loaded &&
                ipv4HistoryState.addressHistory.isEmpty() &&
                ipv6HistoryState is Disabled ||
                ipv6HistoryState is Loaded &&
                ipv6HistoryState.addressHistory.isEmpty() &&
                ipv4HistoryState is Disabled ||
                ipv4HistoryState is Loaded &&
                ipv4HistoryState.addressHistory.isEmpty() &&
                ipv6HistoryState is Loaded &&
                ipv6HistoryState.addressHistory.isEmpty()
            )

    if (shouldShowNoPermission) {
        return NoPermissionScreen(
            modifier = modifier,
            onGrantPermission = onGrantPermission
        )
    }

    if (
        ipv4HistoryState is Loaded &&
        ipv6HistoryState is Loaded
    ) {
        return TabAddressHistoryList(
            modifier = modifier,
            ipv4History = ipv4HistoryState.addressHistory,
            ipv6History = ipv6HistoryState.addressHistory,
            hasPermission = hasPermission,
            onGrantPermission = onGrantPermission
        )
    }

    val state = ipv4HistoryState as? Loaded
        ?: ipv6HistoryState as? Loaded

    if (state != null) {
        return Column(modifier.windowInsetsPadding(WindowInsets.statusBars)) {
            if (!hasPermission) {
                AddressHistoryDisabledCard(
                    modifier = Modifier.fillMaxWidth(),
                    onGrantPermission = onGrantPermission
                )
            }
            AddressHistoryList(
                modifier = Modifier.fillMaxSize(),
                items = state.addressHistory
            )
        }
    }
}

@Composable
private fun NoPermissionScreen(onGrantPermission: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.clickable(onClick = onGrantPermission),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(Res.string.history_no_permission_description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(Res.string.tap_to_enable),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun TabAddressHistoryList(
    ipv4History: List<AddressHistory>,
    ipv6History: List<AddressHistory>,
    hasPermission: Boolean,
    onGrantPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.windowInsetsPadding(WindowInsets.statusBars)) {
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
                    text = stringResource(Res.string.ipv4)
                )
            }
            Tab(
                selected = pagerState.currentPage == 1,
                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) } }
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(Res.string.ipv6)
                )
            }
        }

        if (!hasPermission) {
            AddressHistoryDisabledCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .padding(horizontal = 4.dp),
                onGrantPermission = onGrantPermission
            )
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

@Composable
private fun AddressHistoryDisabledCard(
    onGrantPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onGrantPermission),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.error
        )
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(Res.string.history_disabled_card_description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify
        )
    }
}
