package com.maksimowiczm.findmyip.settings.addresshistory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme

@Composable
internal fun AddressHistorySettingsDescription(modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.history),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.history_description),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@PreviewLightDark
@Composable
private fun AddressHistorySettingsDescriptionPreview() {
    FindMyIpAppTheme {
        Surface {
            AddressHistorySettingsDescription()
        }
    }
}
