package com.maksimowiczm.findmyip.ui.history

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.ui.ext.toDp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import findmyip.composeapp.generated.resources.*
import findmyip.composeapp.generated.resources.Res
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryScreen(modifier: Modifier = Modifier, viewModel: HistoryViewModel = koinViewModel()) {
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()

    val ipv4Enabled by viewModel.ipv4Enabled.collectAsStateWithLifecycle()
    val ipv4History = viewModel.ipv4data.collectAsLazyPagingItems()

    val ipv6Enabled by viewModel.ipv6Enabled.collectAsStateWithLifecycle()
    val ipv6History = viewModel.ipv6data.collectAsLazyPagingItems()

    HistoryScreen(
        enabled = enabled,
        onGrantPermission = viewModel::onPermissionGranted,
        ipv4Enabled = ipv4Enabled,
        ipv4History = ipv4History,
        ipv6Enabled = ipv6Enabled,
        ipv6History = ipv6History,
        formatDate = viewModel::formatDate,
        modifier = modifier
    )
}

@Composable
private fun HistoryScreen(
    enabled: Boolean,
    onGrantPermission: () -> Unit,
    ipv4Enabled: Boolean,
    ipv4History: LazyPagingItems<Address>,
    ipv6Enabled: Boolean,
    ipv6History: LazyPagingItems<Address>,
    formatDate: (LocalDateTime) -> String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
    ) {
        if (ipv4History.loadState.source.refresh == LoadState.Loading ||
            ipv6History.loadState.source.refresh == LoadState.Loading
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .safeContentPadding(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (!enabled && ipv4History.itemCount == 0 && ipv6History.itemCount == 0) {
            HistoryDisabledScreen(
                onGrantPermission = onGrantPermission
            )
        } else {
            HistoryScreenWithList(
                hasPermission = enabled,
                ipv4Enabled = ipv4Enabled,
                ipv4History = ipv4History,
                ipv6Enabled = ipv6Enabled,
                ipv6History = ipv6History,
                formatDate = formatDate
            )
        }
    }
}

@Composable
private fun HistoryScreenWithList(
    hasPermission: Boolean,
    ipv4Enabled: Boolean,
    ipv4History: LazyPagingItems<Address>,
    ipv6Enabled: Boolean,
    ipv6History: LazyPagingItems<Address>,
    formatDate: (LocalDateTime) -> String,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val topPadding = WindowInsets.systemBars.only(WindowInsetsSides.Top)
        .union(WindowInsets.navigationBars.only(WindowInsetsSides.Top))
        .union(WindowInsets.displayCutout.only(WindowInsetsSides.Top))

    val horizontalPadding = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
        .union(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
        .union(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal))

    val showIpv4 = ipv4Enabled || ipv4History.itemCount > 0
    val showIpv6 = ipv6Enabled || ipv6History.itemCount > 0

    val pagerState = rememberPagerState(
        initialPage = if (showIpv4 && !showIpv6) {
            0
        } else if (!showIpv4 && showIpv6) {
            1
        } else {
            0
        }
    ) { 2 }

    val applyTopPaddingToList = (showIpv4 && showIpv6) || !hasPermission

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(horizontalPadding)
            .consumeWindowInsets(horizontalPadding)
    ) {
        if (showIpv4 && showIpv6) {
            TabRow(
                modifier = Modifier
                    .windowInsetsPadding(topPadding)
                    .consumeWindowInsets(topPadding),
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
        }

        if (!hasPermission) {
            AddressHistoryDisabledCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .then(
                        if (showIpv4 && showIpv6) {
                            Modifier
                        } else {
                            Modifier.windowInsetsPadding(topPadding)
                        }
                    )
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top,
            userScrollEnabled = showIpv4 && showIpv6
        ) {
            val contentPadding = if (applyTopPaddingToList) {
                PaddingValues(0.dp)
            } else {
                topPadding.asPaddingValues()
            }

            when (it) {
                0 -> HistoryList(
                    enabled = ipv4Enabled,
                    items = ipv4History,
                    formatDate = formatDate,
                    contentPadding = contentPadding
                )

                1 -> HistoryList(
                    enabled = ipv6Enabled,
                    items = ipv6History,
                    formatDate = formatDate,
                    contentPadding = contentPadding
                )
            }
        }
    }
}

@Composable
private fun AddressHistoryDisabledCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.headline_address_history_disabled),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(
                    Res.string.neutral_enable_address_history_in_settings
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HistoryList(
    enabled: Boolean,
    items: LazyPagingItems<Address>,
    formatDate: (LocalDateTime) -> String,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val shimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.Window
    )

    val disabledCard = @Composable {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.headline_ip_version_disabled),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(
                        Res.string.neutral_enable_ip_version_in_settings
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    if (items.itemCount == 0) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(Res.string.headline_empty))
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding
        ) {
            stickyHeader {
                if (!enabled) {
                    disabledCard()
                }
            }

            items(
                count = items.itemCount,
                key = items.itemKey()
            ) { index ->
                val item = items[index]

                Crossfade(
                    targetState = item
                ) {
                    if (it == null) {
                        AddressHistoryListItemSkeleton(
                            shimmer = shimmer
                        )
                    } else {
                        AddressHistoryListItem(
                            ip = it.ip,
                            date = formatDate(it.date)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddressHistoryListItemSkeleton(shimmer: Shimmer, modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Column {
                Spacer(
                    Modifier
                        .shimmer(shimmer)
                        .height(LocalTextStyle.current.toDp() - 2.dp)
                        .width(100.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                )
                Spacer(Modifier.height(2.dp))
            }
        },
        supportingContent = {
            Spacer(
                Modifier
                    .shimmer(shimmer)
                    .height(LocalTextStyle.current.toDp())
                    .width(180.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            )
        }
    )
}

@Composable
private fun AddressHistoryListItem(ip: String, date: String, modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier,
        headlineContent = { Text(text = ip) },
        supportingContent = { Text(text = date) }
    )
}
