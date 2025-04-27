package com.maksimowiczm.findmyip.infrastructure.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.maksimowiczm.findmyip.ui.FindMyIPApp
import com.maksimowiczm.findmyip.ui.theme.FindMyIPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FindMyIPTheme {
                FindMyIPApp()
            }
        }
    }
}
