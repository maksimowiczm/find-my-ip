package com.maksimowiczm.findmyip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.lifecycle.lifecycleScope
import com.maksimowiczm.findmyip.domain.TestInternetProtocolsUseCase
import com.maksimowiczm.findmyip.ui.FindMyIpApp
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var testInternetProtocolsUseCase: TestInternetProtocolsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            testInternetProtocolsUseCase()
        }

        enableEdgeToEdge()
        setContent {
            FindMyIpAppTheme {
                Surface {
                    FindMyIpApp()
                }
            }
        }
    }
}
