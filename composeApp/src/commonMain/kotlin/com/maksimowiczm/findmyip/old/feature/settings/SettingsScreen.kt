package com.maksimowiczm.findmyip.old.feature.settings

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
import com.maksimowiczm.findmyip.old.feature.settings.addresshistory.AddressHistorySettingsDescription
import com.maksimowiczm.findmyip.old.feature.settings.addresshistory.ClearHistorySetting
import com.maksimowiczm.findmyip.old.feature.settings.addresshistory.SaveAddressHistorySettings
import com.maksimowiczm.findmyip.old.feature.settings.internetprotocolversion.InternetProtocolVersionSettings
import com.maksimowiczm.findmyip.old.feature.settings.internetprotocolversion.InternetProtocolVersionSettingsDescription
import com.maksimowiczm.findmyip.settings.Setting
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.stringResource

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
        TopAppBar(title = { Text(stringResource(Res.string.settings)) })
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
