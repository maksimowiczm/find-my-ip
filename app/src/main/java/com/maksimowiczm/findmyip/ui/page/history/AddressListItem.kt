package com.maksimowiczm.findmyip.ui.page.history

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.maksimowiczm.findmyip.domain.model.NetworkType
import com.maksimowiczm.findmyip.ui.res.stringResource
import com.maksimowiczm.findmyip.ui.theme.FindMyIPTheme
import com.maksimowiczm.findmyip.ui.utils.LocalDateFormatter
import kotlinx.datetime.LocalDateTime

@Composable
fun AddressListItem(
    address: Address,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    colors: ListItemColors = ListItemDefaults.colors()
) {
    val dateFormatter = LocalDateFormatter.current

    val networkStr = address.networkType.stringResource()
    val supportingText = remember(dateFormatter, address) {
        buildString {
            append(dateFormatter.formatDateTime(address.dateTime))
            append(", ")
            append(networkStr)
        }
    }

    ListItem(
        headlineContent = {
            Text(address.ip)
        },
        modifier = Modifier
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .then(modifier),
        supportingContent = {
            Text(supportingText)
        },
        colors = colors
    )
}

@Preview
@Composable
private fun AddressListItemPreview() {
    FindMyIPTheme {
        AddressListItem(
            address = Address(
                ip = "192.168.1.1",
                dateTime = LocalDateTime(2023, 10, 1, 12, 0),
                networkType = NetworkType.WiFi
            ),
            onClick = {}
        )
    }
}
