package com.maksimowiczm.findmyip.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.maksimowiczm.findmyip.presentation.home.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoute(
    onSettings: () -> Unit,
    onVolunteer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val pages = viewModel.history.collectAsLazyPagingItems()
    val ip4 by viewModel.ipv4.collectAsStateWithLifecycle()
    val ip6 by viewModel.ipv6.collectAsStateWithLifecycle()
    val filter by viewModel.filter.collectAsStateWithLifecycle()

    HomeScreen(
        ip4 = ip4,
        ip6 = ip6,
        history = pages,
        filter = filter,
        isRefreshing = isRefreshing,
        onRefresh = viewModel::refresh,
        onSearch = viewModel::search,
        onSettings = onSettings,
        onVolunteer = onVolunteer,
        onFilterUpdate = viewModel::updateFilter,
        modifier = modifier,
    )
}
