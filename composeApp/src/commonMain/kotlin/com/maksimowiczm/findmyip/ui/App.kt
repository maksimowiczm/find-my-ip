package com.maksimowiczm.findmyip.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.maksimowiczm.findmyip.shared.ui.FindMyIpTheme

@Composable
fun App() {
    FindMyIpTheme { Surface { AppNavHost() } }
}
