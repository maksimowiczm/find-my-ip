package com.maksimowiczm.findmyip.ui.page.settings.backgroundservices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.ui.component.SwitchSettingListItem
import com.maksimowiczm.findmyip.ui.ext.firstVisibleItemAlpha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundServicesPage(
    onBack: () -> Unit,
    foregroundServiceEnabled: Boolean,
    onToggleForegroundService: (Boolean) -> Unit,
    periodicWorkEnabled: Boolean,
    onTogglePeriodicWork: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var firstItemHeight by remember { mutableIntStateOf(0) }
    val headlineAlpha = lazyListState.firstVisibleItemAlpha(firstItemHeight)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.headline_background_services),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.graphicsLayer { alpha = headlineAlpha }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag(BackgroundServicesPageTestTags.GO_BACK_BUTTON)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_go_back)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .testTag(BackgroundServicesPageTestTags.CONTENT)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            state = lazyListState,
            contentPadding = paddingValues
        ) {
            item {
                Column(
                    modifier = Modifier.onGloballyPositioned {
                        firstItemHeight = it.size.height
                    }
                ) {
                    Spacer(Modifier.height(32.dp))

                    Text(
                        text = stringResource(R.string.headline_background_services),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }

            item {
                Spacer(Modifier.height(16.dp))
            }

            item {
                Text(
                    text = stringResource(R.string.description_background_services_long),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
            }

            item {
                SwitchSettingListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.headline_foreground_service)
                        )
                    },
                    checked = foregroundServiceEnabled,
                    onCheckedChange = onToggleForegroundService,
                    modifier = Modifier.testTag(
                        BackgroundServicesPageTestTags.FOREGROUND_SERVICE_SETTING
                    ),
                    supportingContent = {
                        Text(
                            text = stringResource(R.string.description_foreground_service)
                        )
                    }
                )
            }

            item {
                SwitchSettingListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.headline_periodic_work)
                        )
                    },
                    checked = periodicWorkEnabled,
                    onCheckedChange = onTogglePeriodicWork,
                    modifier = Modifier.testTag(
                        BackgroundServicesPageTestTags.PERIODIC_WORK_SETTING
                    ),
                    supportingContent = {
                        Text(
                            text = stringResource(R.string.description_periodic_work)
                        )
                    }
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .testTag(BackgroundServicesPageTestTags.TIPS)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Tip(
                        title = {
                            Text(
                                text = stringResource(
                                    R.string.headline_foreground_service_and_periodic_work
                                )
                            )
                        },
                        description = {
                            Text(
                                text = stringResource(
                                    R.string.description_foreground_service_and_periodic_work
                                )
                            )
                        }
                    )

                    Tip(
                        title = {
                            Text(
                                text = stringResource(
                                    R.string.headline_what_is_network_state_change
                                )
                            )
                        },
                        description = {
                            Text(
                                text = stringResource(
                                    R.string.description_what_is_network_state_change
                                )
                            )
                        }
                    )

                    Tip(
                        title = {},
                        description = {
                            Text(
                                text = stringResource(
                                    R.string.description_combine_periodic_notifications
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Tip(
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null
                )
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.titleSmall
                ) {
                    title()
                }
            }

            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyMedium
            ) {
                description()
            }
        }
    }
}
