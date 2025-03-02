package com.maksimowiczm.findmyip.ui.history

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.ui.ext.toDp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.history_disabled_card_description
import findmyip.composeapp.generated.resources.ipv4
import findmyip.composeapp.generated.resources.ipv6
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryScreen(modifier: Modifier = Modifier, viewModel: HistoryViewModel = koinViewModel()) {
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()
    val ipv4History = viewModel.ipv4data.collectAsLazyPagingItems()
    val ipv6History = viewModel.ipv6data.collectAsLazyPagingItems()

    HistoryScreen(
        enabled = enabled,
        onGrantPermission = viewModel::onPermissionGranted,
        ipv4History = ipv4History,
        ipv6History = ipv6History,
        formatDate = viewModel::formatDate,
        modifier = modifier
    )
}

@Composable
private fun HistoryScreen(
    enabled: Boolean,
    onGrantPermission: () -> Unit,
    ipv4History: LazyPagingItems<Address>,
    ipv6History: LazyPagingItems<Address>,
    formatDate: (LocalDateTime) -> String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
    ) {
        if (!enabled && ipv4History.itemCount == 0 && ipv6History.itemCount == 0) {
            HistoryDisabledScreen(
                onGrantPermission = onGrantPermission
            )
        } else {
            HistoryScreenWithList(
                hasPermission = enabled,
                onGrantPermission = onGrantPermission,
                ipv4History = ipv4History,
                ipv6History = ipv6History,
                formatDate = formatDate
            )
        }
    }
}

@Composable
private fun HistoryScreenWithList(
    hasPermission: Boolean,
    onGrantPermission: () -> Unit,
    ipv4History: LazyPagingItems<Address>,
    ipv6History: LazyPagingItems<Address>,
    formatDate: (LocalDateTime) -> String,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { 2 }
    val coroutineScope = rememberCoroutineScope()

    val topPadding = WindowInsets.systemBars.only(WindowInsetsSides.Top)
        .union(WindowInsets.navigationBars.only(WindowInsetsSides.Top))
        .union(WindowInsets.displayCutout.only(WindowInsetsSides.Top))

    val horizontalPadding = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
        .union(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
        .union(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal))

    val padding = topPadding.union(horizontalPadding).asPaddingValues()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
            .consumeWindowInsets(padding)
    ) {
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
                onGrantPermission = onGrantPermission,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top
        ) {
            when (it) {
                0 -> HistoryList(
                    items = ipv4History,
                    formatDate = formatDate
                )

                1 -> HistoryList(
                    items = ipv6History,
                    formatDate = formatDate
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
        onClick = onGrantPermission,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.error
        )
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(Res.string.history_disabled_card_description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
private fun HistoryList(
    items: LazyPagingItems<Address>,
    formatDate: (LocalDateTime) -> String,
    modifier: Modifier = Modifier
) {
    val shimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.Window
    )

    LazyColumn(
        modifier = modifier
    ) {
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
