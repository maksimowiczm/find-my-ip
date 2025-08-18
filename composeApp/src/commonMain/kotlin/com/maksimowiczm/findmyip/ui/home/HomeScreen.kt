package com.maksimowiczm.findmyip.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.maksimowiczm.findmyip.domain.entity.AddressHistory
import com.maksimowiczm.findmyip.ui.infrastructure.LocalClipboardManager
import com.maksimowiczm.findmyip.ui.infrastructure.LocalDateFormatter
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.action_refresh
import findmyip.composeapp.generated.resources.error_failed_to_fetch_your_address
import findmyip.composeapp.generated.resources.ipv4
import findmyip.composeapp.generated.resources.ipv6
import org.jetbrains.compose.resources.stringResource

@Composable
fun CurrentAddressScreen(
    history: LazyPagingItems<AddressHistory>,
    isRefreshing: Boolean,
    isError: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        modifier = modifier,
        topBar = { TopBar(isLoading = isRefreshing, onRefresh = onRefresh) },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier.fillMaxSize()
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyColumn {
                item {
                    Box {
                        this@Column.AnimatedVisibility(
                            visible = isError,
                            enter = expandVertically(),
                            exit = shrinkVertically(),
                        ) {
                            ErrorCard()
                        }
                    }
                }

                items(count = history.itemCount, key = history.itemKey { it.id }) {
                    val item = history[it]

                    if (item == null) {
                        // TODO loading state
                        return@items
                    }

                    AddressButton(history = item, onClick = {})
                }
            }
        }
    }
}

@Composable
private fun ErrorCard(modifier: Modifier = Modifier) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            ),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Outlined.ErrorOutline, null)
            Text(
                text = stringResource(Res.string.error_failed_to_fetch_your_address),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AddressButton(
    history: AddressHistory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = LocalDateFormatter.current

    val label =
        when (history) {
            is AddressHistory.Ipv4 -> stringResource(Res.string.ipv4)
            is AddressHistory.Ipv6 -> stringResource(Res.string.ipv6)
        }
    val date = history.dateTime
    val address =
        when (history) {
            is AddressHistory.Ipv4 -> history.address.stringRepresentation()
            is AddressHistory.Ipv6 -> history.address.stringRepresentation()
        }

    Button(
        onClick = onClick,
        modifier = modifier,
        shapes = ButtonDefaults.shapes(shape = MaterialTheme.shapes.extraLarge),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = label, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = dateFormatter.formatDateTimeLong(date),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Text(
                text = address,
                style = MaterialTheme.typography.headlineMediumEmphasized,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TopBar(isLoading: Boolean, onRefresh: () -> Unit, modifier: Modifier = Modifier) {
    val stateTransition = updateTransition(isLoading)

    val insets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
    val motionScheme = MaterialTheme.motionScheme

    Column(modifier = modifier.windowInsetsPadding(insets).consumeWindowInsets(insets)) {
        stateTransition.AnimatedContent(
            contentKey = { it },
            transitionSpec = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    motionScheme.slowEffectsSpec(),
                ) togetherWith
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        motionScheme.slowEffectsSpec(),
                    )
            },
        ) {
            if (it) {
                LinearWavyProgressIndicator(modifier = Modifier.fillMaxWidth())
            } else {
                Spacer(Modifier.height(10.dp))
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            FilledTonalIconButton(onClick = onRefresh, shapes = IconButtonDefaults.shapes()) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = stringResource(Res.string.action_refresh),
                )
            }
        }
    }
}
