package com.maksimowiczm.findmyip.ui.currentaddress

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.presentation.currentaddress.CurrentAddressViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CurrentAddressRoute(
    modifier: Modifier = Modifier,
    viewModel: CurrentAddressViewModel = koinViewModel(),
) {
    val uiState by viewModel.ip4Flow.collectAsStateWithLifecycle()

    CurrentAddressScreen(uiState = uiState, onRefresh = viewModel::refresh, modifier = modifier)
}
