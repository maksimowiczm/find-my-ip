package com.maksimowiczm.whatismyip.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.maksimowiczm.whatismyip.ui.theme.RememberIPTheme

@Composable
fun RememberIPApp() {
    RememberIPTheme {
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                RememberIPNavigation()
            }
        }
    }
}
