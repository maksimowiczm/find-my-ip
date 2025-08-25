package com.maksimowiczm.findmyip.feature.background.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.feature.background.presentation.BackgroundWorkViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RunInBackgroundRoute(onBack: () -> Unit, modifier: Modifier = Modifier.Companion) {
    val viewModel: BackgroundWorkViewModel = koinViewModel()

    val periodicWorkerRunning by viewModel.periodicWorkerRunning.collectAsStateWithLifecycle()

    RunInBackgroundScreen(
        onBack = onBack,
        periodicRefreshRunning = periodicWorkerRunning,
        onTogglePeriodicRefresh = viewModel::togglePeriodicWorker,
        modifier = modifier,
    )
}
