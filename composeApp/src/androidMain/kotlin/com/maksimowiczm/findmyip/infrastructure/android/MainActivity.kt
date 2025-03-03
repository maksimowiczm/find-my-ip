package com.maksimowiczm.findmyip.infrastructure.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.maksimowiczm.findmyip.FindMyIpApp
import com.maksimowiczm.findmyip.data.initializer.AppInitializer
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val appInitializer by inject<AppInitializer>()
            appInitializer()
        }

        setContent {
            FindMyIpAppTheme {
                FindMyIpApp()
            }
        }
    }
}
