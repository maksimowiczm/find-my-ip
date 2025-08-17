package com.maksimowiczm.findmyip.ui.currentaddress

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.presentation.currentaddress.CurrentAddressViewModel

@Composable
fun CurrentAddressRoute(viewModel: CurrentAddressViewModel, modifier: Modifier = Modifier) {
    val ip4 by viewModel.ip4Flow.collectAsStateWithLifecycle()

    CurrentAddressScreen(ip4 = ip4, onRefresh = viewModel::refresh, modifier = modifier)
}
