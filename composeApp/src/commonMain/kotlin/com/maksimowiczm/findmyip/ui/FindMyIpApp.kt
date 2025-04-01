package com.maksimowiczm.findmyip.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.maksimowiczm.findmyip.navigation.FindMyIpNavHost

@Composable
fun FindMyIpApp(modifier: Modifier = Modifier) {
    FindMyIpNavHost(
        modifier = modifier
    )
}
