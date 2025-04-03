package com.maksimowiczm.findmyip.feature.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.maksimowiczm.findmyip.ext.toDp
import com.maksimowiczm.findmyip.ui.res.stringResource
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryScreen(
    onHistorySettingsClick: () -> Unit,
    onInternetProtocolSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = koinViewModel()
) {
    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val history = viewModel.history.collectAsLazyPagingItems()
    val showIpv4 by viewModel.showIpv4.collectAsStateWithLifecycle()
    val showIpv6 by viewModel.showIpv6.collectAsStateWithLifecycle()
    val ipv4Enabled by viewModel.ipv4Enabled.collectAsStateWithLifecycle()
    val ipv6Enabled by viewModel.ipv6Enabled.collectAsStateWithLifecycle()
    val historyEnabled by viewModel.historyEnabled.collectAsStateWithLifecycle()

    HistoryScreen(
        selectedFilter = filter,
        history = history,
        historyEnabled = historyEnabled,
        showIpv4 = showIpv4,
        showIpv6 = showIpv6,
        ipv4Enabled = ipv4Enabled,
        ipv6Enabled = ipv6Enabled,
        onFilterSelect = remember(viewModel) { viewModel::setFilter },
        onHistorySettingsClick = onHistorySettingsClick,
        onInternetProtocolSettingsClick = onInternetProtocolSettingsClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun HistoryScreen(
    selectedFilter: Filter,
    history: LazyPagingItems<HistoryItem>,
    historyEnabled: Boolean,
    showIpv4: Boolean,
    showIpv6: Boolean,
    ipv4Enabled: Boolean,
    ipv6Enabled: Boolean,
    onFilterSelect: (Filter) -> Unit,
    onHistorySettingsClick: () -> Unit,
    onInternetProtocolSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topPadding = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Top)
    val shimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.Window
    )

    val filters = remember(showIpv4, showIpv6) {
        Filter.entries.filter { filter ->
            when (filter) {
                Filter.Ipv4 -> showIpv4
                Filter.Ipv6 -> showIpv6
                else -> true
            }
        }
    }
    val shouldShowFilters = remember(filters) {
        filters.filterNot { it == Filter.All }.size > 1
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(topPadding)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .windowInsetsPadding(topPadding)
                .consumeWindowInsets(topPadding)
        ) {
            Text(
                text = stringResource(Res.string.headline_history),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (shouldShowFilters) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filters.forEach { filter ->
                        item {
                            FilterChip(
                                selected = filter == selectedFilter,
                                onClick = { onFilterSelect(filter) },
                                label = {
                                    Text(
                                        text = stringResource(filter.filterName)
                                    )
                                },
                                leadingIcon = {
                                    AnimatedVisibility(
                                        visible = filter == selectedFilter,
                                        enter = expandHorizontally() + fadeIn(snap(0)),
                                        exit = shrinkHorizontally() + fadeOut(snap(0))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                HorizontalDivider()
            }

            if (!historyEnabled) {
                HistoryDisabledCard(
                    onClick = onHistorySettingsClick,
                    modifier = Modifier.padding(8.dp)
                )
            }

            if (history.itemCount == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.headline_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            LazyColumn(
                contentPadding = paddingValues
            ) {
                stickyHeader {
                    Box {
                        val showCard = (
                            (selectedFilter == Filter.Ipv4 && !ipv4Enabled) ||
                                (selectedFilter == Filter.Ipv6 && !ipv6Enabled)
                            ) &&
                            historyEnabled

                        this@Column.AnimatedVisibility(
                            visible = showCard
                        ) {
                            IpVersionDisabledCard(
                                onClick = onInternetProtocolSettingsClick,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

                items(
                    count = history.itemCount,
                    key = history.itemKey { it.id }
                ) {
                    val item = history[it]

                    val transition = updateTransition(item)

                    transition.Crossfade(
                        contentKey = { it != null }
                    ) {
                        if (it != null) {
                            it.ListItem()
                        } else {
                            ListItemSkeleton(shimmer)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryItem.ListItem(modifier: Modifier = Modifier) {
    ListItem(
        headlineContent = {
            Text(
                text = ip,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        modifier = modifier,
        supportingContent = {
            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        trailingContent = {
            Text(
                text = protocol.stringResource(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    )
}

@Composable
private fun ListItemSkeleton(shimmer: Shimmer, modifier: Modifier = Modifier) {
    ListItem(
        headlineContent = {
            Spacer(
                modifier = Modifier
                    .shimmer(shimmer)
                    .clip(MaterialTheme.shapes.small)
                    .height(MaterialTheme.typography.bodyLarge.toDp() - 4.dp)
                    .width(100.dp)
                    .padding(vertical = 2.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            )
        },
        modifier = modifier,
        supportingContent = {
            Spacer(
                modifier = Modifier
                    .shimmer(shimmer)
                    .clip(MaterialTheme.shapes.small)
                    .height(MaterialTheme.typography.bodyMedium.toDp() - 4.dp)
                    .width(120.dp)
                    .padding(vertical = 2.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            )
        },
        trailingContent = {
            Spacer(
                modifier = Modifier
                    .shimmer(shimmer)
                    .clip(MaterialTheme.shapes.small)
                    .height(MaterialTheme.typography.bodyMedium.toDp())
                    .width(40.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            )
        }
    )
}

@Composable
private fun HistoryDisabledCard(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.headline_address_history_is_disabled),
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = stringResource(Res.string.neutral_enable_address_history_in_settings),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun IpVersionDisabledCard(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.headline_ip_version_disabled),
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = stringResource(Res.string.neutral_enable_ip_version_in_settings),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
