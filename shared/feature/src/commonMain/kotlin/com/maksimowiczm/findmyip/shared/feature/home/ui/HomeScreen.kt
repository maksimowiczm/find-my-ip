package com.maksimowiczm.findmyip.shared.feature.home.ui

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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.maksimowiczm.findmyip.shared.core.feature.ui.LocalClipboardManager
import com.maksimowiczm.findmyip.shared.feature.home.persentation.AddressHistoryUiModel
import com.maksimowiczm.findmyip.shared.feature.home.persentation.CurrentAddressUiModel
import com.maksimowiczm.findmyip.shared.feature.home.persentation.Filter
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.headline_current
import findmyip.composeapp.generated.resources.headline_history
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import org.jetbrains.compose.resources.stringResource

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    FlowPreview::class,
)
@Composable
internal fun HomeScreen(
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
