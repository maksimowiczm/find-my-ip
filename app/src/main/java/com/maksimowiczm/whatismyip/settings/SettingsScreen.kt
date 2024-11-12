package com.maksimowiczm.whatismyip.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.maksimowiczm.whatismyip.R
import com.maksimowiczm.whatismyip.addresshistory.HistorySettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(modifier: Modifier = Modifier.fillMaxSize()) {
    Column(modifier) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.settings),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        )

        HistorySettings()
        HorizontalDivider()
    }
}
