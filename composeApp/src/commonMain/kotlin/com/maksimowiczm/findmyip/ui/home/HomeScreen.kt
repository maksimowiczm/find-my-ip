package com.maksimowiczm.findmyip.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.maksimowiczm.findmyip.infrastructure.fake.CommonAddresses
import com.maksimowiczm.findmyip.presentation.home.AddressHistoryUiModel
import com.maksimowiczm.findmyip.presentation.home.CurrentAddressUiModel
import com.maksimowiczm.findmyip.presentation.home.Filter
import com.maksimowiczm.findmyip.presentation.home.InternetProtocolVersion
import com.maksimowiczm.findmyip.presentation.home.NetworkType
import com.maksimowiczm.findmyip.ui.infrastructure.LocalClipboardManager
import com.maksimowiczm.findmyip.ui.shared.FindMyIpTheme
import findmyip.composeapp.generated.resources.*
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    FlowPreview::class,
)
@Composable
fun HomeScreen(
    ip4: CurrentAddressUiModel,
    ip6: CurrentAddressUiModel,
    history: LazyPagingItems<AddressHistoryUiModel>,
    filter: Filter,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onSearch: (String) -> Unit,
    onVolunteer: () -> Unit,
    onSettings: () -> Unit,
    onFilterUpdate: (Filter) -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
    val pullState = rememberPullToRefreshState()

    var showFilters by rememberSaveable { mutableStateOf(false) }
    if (showFilters) {
        FiltersModal(
            filter = filter,
            onDismiss = { showFilters = false },
            onUpdateFilter = onFilterUpdate,
        )
    }

    val searchTextState = rememberTextFieldState()
    LaunchedEffect(searchTextState, onSearch) {
        snapshotFlow { searchTextState.text }
            .debounce(100)
            .collectLatest { onSearch(it.toString()) }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            HomeTopBar(
                filter = filter,
                searchTextState = searchTextState,
                onSearch = onSearch,
                onVolunteer = onVolunteer,
                onSettings = onSettings,
                onFilter = { showFilters = true },
            )
        },
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = pullState,
            indicator = {
                MyCustomIndicator(
                    state = pullState,
                    isRefreshing = isRefreshing,
                    contentPadding = PaddingValues(top = paddingValues.calculateTopPadding()),
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            },
        ) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(360.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = paddingValues.add(horizontal = 16.dp),
            ) {
                if (ip4 is CurrentAddressUiModel.Address || ip6 is CurrentAddressUiModel.Address) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Text(
                            text = stringResource(Res.string.headline_current),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                    }
                }

                if (ip4 is CurrentAddressUiModel.Address) {
                    item(key = "ip4") {
                        AddressButton(
                            model = ip4,
                            onClick = { clipboardManager.copyToClipboard(ip4.address) },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                    }
                }

                if (ip6 is CurrentAddressUiModel.Address) {
                    item(key = "ip6") {
                        AddressButton(
                            model = ip6,
                            onClick = { clipboardManager.copyToClipboard(ip6.address) },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                    }
                }

                if (history.itemCount > 0) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Text(
                            text = stringResource(Res.string.headline_history),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                    }
                }

                items(count = history.itemCount, key = history.itemKey { it.id }) {
                    val item = history[it] ?: return@items

                    AddressButton(
                        model = item,
                        onClick = { clipboardManager.copyToClipboard(item.address) },
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.animateItem().padding(bottom = 8.dp),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MyCustomIndicator(
    state: PullToRefreshState,
    isRefreshing: Boolean,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val topPadding = contentPadding.calculateTopPadding()

    Box(
        modifier =
            modifier.graphicsLayer {
                alpha = if (state.distanceFraction == 0f) 0f else 1f
                translationY =
                    topPadding.roundToPx() - size.height +
                        state.distanceFraction * size.height +
                        8.dp.roundToPx()
            },
        contentAlignment = Alignment.Center,
    ) {
        if (isRefreshing) {
            ContainedLoadingIndicator(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                indicatorColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        } else {
            Surface(
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = null,
                        modifier =
                            Modifier.size(32.dp).graphicsLayer {
                                this.rotationZ = state.distanceFraction * 90f
                            },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    val now = LocalDateTime(2025, 8, 22, 15, 34, 11)
    val ip4 =
        CurrentAddressUiModel.Address(
            address = CommonAddresses.GOOGLE_V4_1,
            domain = "google.com",
            dateTime = now,
            networkType = NetworkType.WIFI,
            internetProtocolVersion = InternetProtocolVersion.IPV4,
        )

    val ip6 =
        CurrentAddressUiModel.Address(
            address = CommonAddresses.GOOGLE_V6,
            domain = "google.com",
            dateTime = now,
            networkType = NetworkType.WIFI,
            internetProtocolVersion = InternetProtocolVersion.IPV6,
        )

    val history =
        flowOf(
                PagingData.from(
                    listOf(
                            AddressHistoryUiModel(
                                id = 1,
                                address = CommonAddresses.GITHUB_V4_1,
                                domain = null,
                                networkType = NetworkType.WIFI,
                                dateTime = now.minus(1.hours + 1.minutes + 43.seconds),
                                internetProtocolVersion = InternetProtocolVersion.IPV4,
                            ),
                            AddressHistoryUiModel(
                                id = 2,
                                address = CommonAddresses.GITHUB_V4_2,
                                networkType = NetworkType.CELLULAR,
                                domain = null,
                                dateTime = now.minus(2.hours + 5.minutes + 29.seconds),
                                internetProtocolVersion = InternetProtocolVersion.IPV4,
                            ),
                            AddressHistoryUiModel(
                                id = 3,
                                address = CommonAddresses.GOOGLE_V6,
                                networkType = NetworkType.UNKNOWN,
                                domain = "google.com",
                                dateTime = now.minus(5.hours + 12.minutes + 10.seconds),
                                internetProtocolVersion = InternetProtocolVersion.IPV6,
                            ),
                            AddressHistoryUiModel(
                                id = 4,
                                address = CommonAddresses.CHATGPT_V6,
                                networkType = NetworkType.VPN,
                                domain = null,
                                dateTime = now.minus(1.days + 6.hours + 22.minutes + 30.seconds),
                                internetProtocolVersion = InternetProtocolVersion.IPV6,
                            ),
                            AddressHistoryUiModel(
                                id = 5,
                                address = CommonAddresses.CHATGPT_V4_1,
                                networkType = NetworkType.WIFI,
                                domain = null,
                                dateTime = now.minus(3.days + 2.hours + 15.minutes + 5.seconds),
                                internetProtocolVersion = InternetProtocolVersion.IPV4,
                            ),
                            AddressHistoryUiModel(
                                id = 6,
                                address = CommonAddresses.CHATGPT_V4_2,
                                networkType = NetworkType.WIFI,
                                domain = null,
                                dateTime = now.minus(3.days + 2.hours + 10.minutes + 22.seconds),
                                internetProtocolVersion = InternetProtocolVersion.IPV4,
                            ),
                            AddressHistoryUiModel(
                                id = 7,
                                address = CommonAddresses.ANDROID_V6,
                                networkType = NetworkType.CELLULAR,
                                domain = null,
                                dateTime = now.minus(3.days + 5.hours + 45.minutes + 32.seconds),
                                internetProtocolVersion = InternetProtocolVersion.IPV6,
                            ),
                        )
                        .sortedByDescending { it.dateTime }
                )
            )
            .collectAsLazyPagingItems()

    FindMyIpTheme {
        HomeScreen(
            ip4 = ip4,
            ip6 = ip6,
            history = history,
            filter = Filter(setOf()),
            isRefreshing = false,
            onRefresh = {},
            onSearch = {},
            onVolunteer = {},
            onSettings = {},
            onFilterUpdate = {},
        )
    }
}
