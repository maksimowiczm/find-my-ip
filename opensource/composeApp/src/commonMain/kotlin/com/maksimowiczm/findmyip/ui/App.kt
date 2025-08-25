package com.maksimowiczm.findmyip.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.maksimowiczm.findmyip.navigation.AppNavHost
import com.maksimowiczm.findmyip.shared.core.feature.ui.FindMyIpTheme

@Composable
internal fun App() {
    FindMyIpTheme { Surface { AppNavHost() } }
}
