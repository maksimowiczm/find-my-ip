package com.maksimowiczm.findmyip.feature.settings.addresshistory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.history
import findmyip.composeapp.generated.resources.history_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddressHistorySettingsDescription(modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(Res.string.history),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(Res.string.history_description),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
