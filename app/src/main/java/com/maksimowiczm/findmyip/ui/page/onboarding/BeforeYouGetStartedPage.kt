package com.maksimowiczm.findmyip.ui.page.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.ui.theme.FindMyIPTheme

@Composable
fun BeforeYouGetStartedPage(onAgree: () -> Unit, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    val ipifyUrl = stringResource(R.string.link_ipify)

    BeforeYouGetStartedPage(
        onIpify = { uriHandler.openUri(ipifyUrl) },
        onAgree = onAgree,
        modifier = modifier
    )
}

@Composable
fun BeforeYouGetStartedPage(
    onIpify: () -> Unit,
    onAgree: () -> Unit,
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
                text = stringResource(R.string.onboarding_headline_before_you_get_started),
                style = MaterialTheme.typography.headlineLarge
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
            ) {
                Text(
                    text = stringResource(R.string.onboarding_description_before_you_get_started),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(
                        R.string.onboarding_learn_more_about_ipify_on_their_website
                    ),
                    modifier = Modifier.clickable(
                        enabled = true,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onIpify
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Button(
                onClick = onAgree,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.action_agree_and_continue))
            }
        }
    }
}

@Preview
@Composable
private fun BeforeYouGetStartedPagePreview() {
    FindMyIPTheme {
        BeforeYouGetStartedPage(
            onIpify = {},
            onAgree = {}
        )
    }
}
