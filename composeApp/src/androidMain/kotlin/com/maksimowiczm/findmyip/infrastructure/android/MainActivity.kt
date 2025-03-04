package com.maksimowiczm.findmyip.infrastructure.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.maksimowiczm.findmyip.data.HistoryManager
import com.maksimowiczm.findmyip.ui.FindMyIpApp
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val historyManager by inject<HistoryManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            historyManager.run()
        }

        enableEdgeToEdge()
        setContent {
            FindMyIpAppTheme {
                FindMyIpApp()
            }
        }
    }
}
