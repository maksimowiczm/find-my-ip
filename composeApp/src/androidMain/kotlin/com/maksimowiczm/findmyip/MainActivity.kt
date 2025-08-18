package com.maksimowiczm.findmyip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.maksimowiczm.findmyip.ui.infrastructure.AndroidClipboardManager
import com.maksimowiczm.findmyip.ui.infrastructure.AndroidDateFormatter
import com.maksimowiczm.findmyip.ui.infrastructure.ProvideClipboardManager
import com.maksimowiczm.findmyip.ui.infrastructure.ProvideDateFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ProvideDateFormatter(AndroidDateFormatter(this@MainActivity)) {
                ProvideClipboardManager(AndroidClipboardManager(this@MainActivity)) { App() }
            }
        }
    }
}
