package com.maksimowiczm.findmyip.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.settings.addresshistory.HistorySettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(onHistorySettingsClick: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.settings)) }) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                HistorySettings(onHistorySettingsClick = onHistorySettingsClick)
                HorizontalDivider()
            }
        }
    }
}
