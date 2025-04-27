package com.maksimowiczm.findmyip.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.maksimowiczm.findmyip.R

@Composable
fun FindMyIPApp(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .safeContentPadding(),
            Alignment.Center
        ) {
            Text("Hello " + stringResource(R.string.app_name))
        }
    }
}
