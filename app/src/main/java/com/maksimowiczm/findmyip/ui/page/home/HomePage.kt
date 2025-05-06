package com.maksimowiczm.findmyip.ui.page.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.R
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomePage(modifier: Modifier = Modifier, viewModel: HomePageViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomePage(
        state = state,
        onRefresh = viewModel::onRefresh,
        modifier = modifier
    )
}

@Composable
fun HomePage(state: HomePageState, onRefresh: () -> Unit, modifier: Modifier = Modifier) {
    val stateTransition = updateTransition(state)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                state = state,
                onRefresh = onRefresh
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            stateTransition.AnimatedVisibility(
                visible = { it.noInternetConnection },
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                ErrorCard(Modifier.testTag(HomePageTestTags.ERROR_CARD))
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.neutral_your_public_ip_address_is),
                    style = MaterialTheme.typography.labelLarge
                )

                val shimmer = rememberShimmer(
                    ShimmerBounds.View
                )

                Ipv4Container {
                    state.ipv4.Content(
                        shimmer = shimmer,
                        modifier = Modifier.testTag(state.ipv4.ipv4TestTag)
                    )
                }

                Ipv6Container {
                    state.ipv6.Content(
                        shimmer = shimmer,
                        modifier = Modifier.testTag(state.ipv6.ipv6TestTag)
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(state: HomePageState, onRefresh: () -> Unit, modifier: Modifier = Modifier) {
    val stateTransition = updateTransition(state)

    val insets = WindowInsets.systemBars
        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)

    Column(
        modifier = modifier
            .windowInsetsPadding(insets)
            .consumeWindowInsets(insets)
    ) {
        stateTransition.AnimatedContent(
            contentKey = { it.isLoading },
            transitionSpec = {
                expandVertically() + fadeIn() togetherWith shrinkVertically() + fadeOut()
            }
        ) {
            if (it.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(HomePageTestTags.PROGRESS_INDICATOR)
                )
            } else {
                Spacer(Modifier.height(4.dp))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onRefresh,
                modifier = Modifier.testTag(HomePageTestTags.REFRESH_BUTTON)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.action_refresh)
                )
            }
        }
    }
}

@Composable
private fun ErrorCard(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.error_no_internet_connection),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun Ipv4Container(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    IpContainer(
        label = {
            Text(
                text = stringResource(R.string.ipv4),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = modifier,
        content = content
    )
}

@Composable
private fun Ipv6Container(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    IpContainer(
        label = {
            Text(
                text = stringResource(R.string.ipv6),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = modifier,
        content = content
    )
}

@Composable
private fun IpContainer(
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.weight(1f, false)) {
            content()
        }

        label()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun IpState.Content(shimmer: Shimmer, modifier: Modifier = Modifier) {
    CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.bodyLarge
    ) {
        // Why doesn't this work?
        // LocalTextStyle.current.lineHeight.toDp()
        val density = LocalDensity.current
        val textMeasurer = rememberTextMeasurer()
        val shimmerHeight = with(density) {
            textMeasurer.measure("test", LocalTextStyle.current).size.height.toDp()
        }

        when (this@Content) {
            is IpState.Loading -> when (ip) {
                null -> Box(modifier) {
                    Spacer(
                        modifier = Modifier
                            .shimmer(shimmer)
                            .width(100.dp)
                            .height(shimmerHeight)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    )
                }

                else -> IpText(ip)
            }

            IpState.NotDetected -> Text(
                text = stringResource(R.string.neutral_not_detected),
                modifier = modifier,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            is IpState.Success -> IpText(ip)
        }
    }
}

@Composable
private fun IpText(ip: String, modifier: Modifier = Modifier) {
    Text(
        text = ip,
        modifier = modifier,
        style = if (ip.length > 20) {
            MaterialTheme.typography.bodySmall
        } else {
            LocalTextStyle.current
        }
    )
}
