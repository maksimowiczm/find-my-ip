package com.maksimowiczm.findmyip

import androidx.compose.runtime.Composable
import com.maksimowiczm.findmyip.ui.home.CurrentAddressRoute
import com.maksimowiczm.findmyip.ui.shared.FindMyIpTheme

@OptIn(ExperimentalUnsignedTypes::class)
@Composable
fun App() {
    FindMyIpTheme { CurrentAddressRoute() }
}
