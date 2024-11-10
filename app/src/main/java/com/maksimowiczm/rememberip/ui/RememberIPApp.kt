package com.maksimowiczm.rememberip.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.maksimowiczm.rememberip.ui.theme.RememberIPTheme

@Composable
fun RememberIPApp() {
    RememberIPTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Text(
                text = "Hello, World!",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}