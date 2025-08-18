package com.maksimowiczm.findmyip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.maksimowiczm.findmyip.ui.infrastructure.AndroidClipboardManager
import com.maksimowiczm.findmyip.ui.infrastructure.ProvideClipboardManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent { ProvideClipboardManager(AndroidClipboardManager(this@MainActivity)) { App() } }
    }
}
