package com.maksimowiczm.findmyip.infrastructure.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.maksimowiczm.findmyip.FindMyIpApp
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FindMyIpAppTheme {
                FindMyIpApp()
            }
        }
    }
}
