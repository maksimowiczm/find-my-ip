package com.maksimowiczm.findmyip.infrastructure.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.maksimowiczm.findmyip.ui.FindMyIpApp
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            FindMyIpAppTheme {
                FindMyIpApp()
            }
        }
    }
}
