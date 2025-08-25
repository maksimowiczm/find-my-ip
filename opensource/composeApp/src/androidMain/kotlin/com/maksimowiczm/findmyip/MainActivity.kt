package com.maksimowiczm.findmyip

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.maksimowiczm.findmyip.shared.core.feature.ui.ProvideUtilities
import com.maksimowiczm.findmyip.shared.core.presentation.AndroidClipboardManager
import com.maksimowiczm.findmyip.shared.core.presentation.AndroidDateFormatter
import com.maksimowiczm.findmyip.ui.App

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ProvideUtilities(
                dateFormatter = AndroidDateFormatter(this),
                clipboardManager = AndroidClipboardManager(this),
                content = { App() },
            )
        }
    }
}
