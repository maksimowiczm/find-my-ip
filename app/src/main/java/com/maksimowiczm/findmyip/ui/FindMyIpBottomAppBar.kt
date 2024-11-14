package com.maksimowiczm.findmyip.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme

enum class BottomNavItem {
    Home,
    AddressHistory,
    Settings
}

@Composable
fun FindMyIpBottomAppBar(
    selectedBottomBarItem: BottomNavItem?,
    onHomeClick: () -> Unit,
    onAddressHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier) {
        NavigationBarItem(
            selected = selectedBottomBarItem == BottomNavItem.Home,
            onClick = onHomeClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(R.string.home)
                )
            },
            label = { Text(stringResource(R.string.home)) }
        )
        NavigationBarItem(
            selected = selectedBottomBarItem == BottomNavItem.AddressHistory,
            onClick = onAddressHistoryClick,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_history_24),
                    contentDescription = stringResource(R.string.history)
                )
            },
            label = { Text(stringResource(R.string.history)) }
        )
        NavigationBarItem(
            selected = selectedBottomBarItem == BottomNavItem.Settings,
            onClick = onSettingsClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.settings)
                )
            },
            label = { Text(stringResource(R.string.settings)) }
        )
    }
}

@PreviewLightDark
@Composable
private fun FindMyIpBottomAppBarPreview() {
    FindMyIpAppTheme {
        Surface {
            FindMyIpBottomAppBar(
                selectedBottomBarItem = BottomNavItem.Home,
                onHomeClick = {},
                onAddressHistoryClick = {},
                onSettingsClick = {}
            )
        }
    }
}
