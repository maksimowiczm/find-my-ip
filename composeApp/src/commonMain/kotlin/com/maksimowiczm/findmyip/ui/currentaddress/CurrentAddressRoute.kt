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
    val ip4 by viewModel.ip4Flow.collectAsStateWithLifecycle()

    CurrentAddressScreen(ip4 = ip4, onRefresh = viewModel::refresh, modifier = modifier)
}
