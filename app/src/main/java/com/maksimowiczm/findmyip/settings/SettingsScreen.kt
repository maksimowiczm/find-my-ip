package com.maksimowiczm.findmyip.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.addresshistory.HistorySettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(modifier: Modifier = Modifier.fillMaxSize()) {
    LazyColumn(modifier) {
        item {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            )
        }
        item {
            HistorySettings()
        }
    }
}
