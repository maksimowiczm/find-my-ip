package com.maksimowiczm.findmyip.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.settings.addresshistory.AddressHistorySettingsDescription
import com.maksimowiczm.findmyip.settings.addresshistory.ClearHistorySetting
import com.maksimowiczm.findmyip.settings.addresshistory.SaveAddressHistorySettings
import com.maksimowiczm.findmyip.settings.internetprotocolversion.InternetProtocolVersionSettings
import com.maksimowiczm.findmyip.settings.internetprotocolversion.InternetProtocolVersionSettingsDescription

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    onHistorySettingsClick: () -> Unit,
    highlightSetting: Setting?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        TopAppBar(title = { Text(stringResource(R.string.settings)) })
        LazyColumn {
            item {
                AddressHistorySettingsDescription()
            }
            item {
                SaveAddressHistorySettings(
                    onHistorySettingsClick = onHistorySettingsClick,
                    highlight = highlightSetting == Setting.SaveHistory
                )
            }
            item {
                ClearHistorySetting()
            }
            item {
                HorizontalDivider()
            }
            item {
                InternetProtocolVersionSettingsDescription()
            }
            item {
                InternetProtocolVersionSettings()
            }
            item {
                HorizontalDivider()
            }
            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
            }
        }
    }
}
