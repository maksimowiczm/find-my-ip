package com.maksimowiczm.findmyip.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.settings.addresshistory.HistorySettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(onHistorySettingsClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.settings)) },
            // These were already set in the parent
            windowInsets = WindowInsets(0.dp)
        )
        LazyColumn {
            item {
                HistorySettings(onHistorySettingsClick = onHistorySettingsClick)
                HorizontalDivider()
            }
        }
    }
}
