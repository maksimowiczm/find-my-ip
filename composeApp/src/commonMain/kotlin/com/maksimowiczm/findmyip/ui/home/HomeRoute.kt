package com.maksimowiczm.findmyip.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.maksimowiczm.findmyip.presentation.home.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoute(modifier: Modifier = Modifier, viewModel: HomeViewModel = koinViewModel()) {
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isError by viewModel.isError.collectAsStateWithLifecycle()
    val pages = viewModel.history.collectAsLazyPagingItems()

    HomeScreen(
        history = pages,
        isRefreshing = isRefreshing,
        isError = isError,
        onRefresh = viewModel::refresh,
        onSearch = {
            // TODO
        },
        onSettings = {
            // TODO
        },
        onVolunteer = {
            // TODO
        },
        modifier = modifier,
    )
}
