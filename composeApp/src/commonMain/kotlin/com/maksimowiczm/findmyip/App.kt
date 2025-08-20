package com.maksimowiczm.findmyip

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.maksimowiczm.findmyip.ui.shared.FindMyIpTheme

@Composable
fun App() {
    FindMyIpTheme { Surface { AppNavHost() } }
}
