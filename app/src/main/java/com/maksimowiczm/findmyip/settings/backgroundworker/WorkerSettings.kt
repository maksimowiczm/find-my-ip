package com.maksimowiczm.findmyip.settings.backgroundworker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.WorkInfo
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.settings.SettingToggle
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme
import kotlin.math.roundToInt

@Composable
internal fun WorkerSettings(
    modifier: Modifier = Modifier,
    viewModel: WorkerSettingsViewModel = hiltViewModel()
) {
    val state by viewModel.workerEnabledState.collectAsStateWithLifecycle()
    val intervalIndex by viewModel.workerIntervalIndex.collectAsStateWithLifecycle()
    val workerStatus by viewModel.workerStatus.collectAsStateWithLifecycle(null)

    WorkerSettings(
        isEnabled = state,
        intervalIndex = intervalIndex,
        intervals = viewModel.intervals,
        onEnable = viewModel::enableWorker,
        onDisable = viewModel::disableWorker,
        workerStatus = workerStatus,
        modifier = modifier
    )
}

@Composable
private fun WorkerSettings(
    isEnabled: Boolean,
    intervalIndex: Int,
    intervals: Array<Long>,
    onEnable: (Int) -> Unit,
    onDisable: () -> Unit,
    workerStatus: WorkInfo.State?,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.history_background_service_title),
            style = MaterialTheme.typography.titleMedium
        )
        WorkerServiceDescription()
        SettingToggle(
            headlineContent = { Text(stringResource(R.string.enable)) },
            checked = isEnabled,
            onCheckedChange = {
                if (it) {
                    onEnable(intervalIndex)
                } else {
                    onDisable()
                }
            },
            enabled = true
        )
        if (isEnabled) {
            WorkerServiceSettings(intervalIndex, intervals, onEnable, workerStatus)
        }
    }
}

@Composable
private fun WorkerServiceSettings(
    intervalIndex: Int,
    intervals: Array<Long>,
    onEnable: (Int) -> Unit,
    workerStatus: WorkInfo.State?,
    modifier: Modifier = Modifier
) {
    var sliderValue by rememberSaveable { mutableFloatStateOf(intervalIndex.toFloat()) }
    val index = { sliderValue.roundToInt() }
    Column(modifier) {
        Slider(
            value = sliderValue,
            onValueChange = {
                sliderValue = it
                onEnable(index())
            },
            valueRange = 0f..intervals.size.toFloat() - 1,
            steps = intervals.size - 2,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.check_every_n_minutes, intervals[index()]),
            style = MaterialTheme.typography.bodyMedium
        )
        if (workerStatus != null) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Service status: $workerStatus",
                style = MaterialTheme.typography.bodyMedium,
                color = when (workerStatus) {
                    WorkInfo.State.ENQUEUED,
                    WorkInfo.State.RUNNING -> MaterialTheme.colorScheme.outline

                    WorkInfo.State.SUCCEEDED,
                    WorkInfo.State.FAILED,
                    WorkInfo.State.BLOCKED,
                    WorkInfo.State.CANCELLED -> MaterialTheme.colorScheme.error
                }
            )
        }
    }
}

@Composable
private fun WorkerServiceDescription() {
    val description = stringResource(R.string.history_background_service_description)
    val link = stringResource(R.string.doze_link)
    val linkColor = MaterialTheme.colorScheme.primary
    val learn = stringResource(R.string.learn_about_doze)

    val annotatedLinkString = remember {
        buildAnnotatedString {
            append(description)
            append(" ")
            withLink(LinkAnnotation.Url(link, TextLinkStyles(SpanStyle(linkColor)))) {
                append(learn)
            }
        }
    }

    Text(
        modifier = Modifier.padding(8.dp),
        text = annotatedLinkString,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Justify
    )
}

@PreviewLightDark
@Composable
private fun WorkerSettingsPreview() {
    FindMyIpAppTheme {
        Surface {
            WorkerSettings(
                isEnabled = true,
                intervalIndex = 1,
                intervals = arrayOf(15, 30, 60, 120, 240, 480, 720, 1440),
                onEnable = {},
                onDisable = {},
                workerStatus = WorkInfo.State.ENQUEUED
            )
        }
    }
}
