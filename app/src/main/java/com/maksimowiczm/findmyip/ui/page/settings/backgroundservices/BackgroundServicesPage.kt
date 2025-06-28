package com.maksimowiczm.findmyip.ui.page.settings.backgroundservices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.ui.component.SwitchSettingListItem
import com.maksimowiczm.findmyip.ui.ext.add
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BackgroundServicesPage(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BackgroundServicesPageViewModel = koinViewModel()
) {
    val periodicWorkEnabled by viewModel.periodicWorkEnabled.collectAsStateWithLifecycle()

    BackgroundServicesPage(
        onBack = onBack,
        periodicWorkEnabled = periodicWorkEnabled,
        onTogglePeriodicWork = viewModel::onTogglePeriodicWork,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundServicesPage(
    onBack: () -> Unit,
    periodicWorkEnabled: Boolean,
    onTogglePeriodicWork: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.headline_background_services),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
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
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            state = lazyListState,
            contentPadding = paddingValues.add(vertical = 8.dp)
        ) {
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
                            text = stringResource(R.string.headline_periodic_work)
                        )
                    },
                    checked = periodicWorkEnabled,
                    onCheckedChange = onTogglePeriodicWork,
                    supportingContent = {
                        Text(
                            text = stringResource(R.string.description_periodic_work)
                        )
                    }
                )
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
