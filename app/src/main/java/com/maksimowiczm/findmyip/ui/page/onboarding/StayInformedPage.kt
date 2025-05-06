package com.maksimowiczm.findmyip.ui.page.onboarding

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.ui.theme.FindMyIPTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StayInformedPage(onEnable: () -> Unit, onSkip: () -> Unit, modifier: Modifier = Modifier) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionState = rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS
        ) {
            if (it) {
                onEnable()
            } else {
                onSkip()
            }
        }

        StayInformedPageImpl(
            onEnable = permissionState::launchPermissionRequest,
            onSkip = onSkip,
            modifier = modifier
        )
    } else {
        StayInformedPageImpl(
            onEnable = onEnable,
            onSkip = onSkip,
            modifier = modifier
        )
    }
}

@Composable
private fun StayInformedPageImpl(
    onEnable: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.onboarding_headline_stay_informed),
                style = MaterialTheme.typography.headlineLarge
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
            ) {
                Text(
                    text = stringResource(R.string.onboarding_description_stay_informed),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Button(
                onClick = onEnable,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.action_enable))
            }

            TextButton(
                onClick = onSkip,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.action_skip_for_now))
            }
        }
    }
}

@Preview
@Composable
private fun StayInformedPagePreview() {
    FindMyIPTheme {
        StayInformedPage(
            onEnable = {},
            onSkip = {},
            modifier = Modifier
        )
    }
}
