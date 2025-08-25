package com.maksimowiczm.findmyip.shared.feature.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Engineering
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.shared.core.feature.ui.ArrowBackIconButton
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun SettingsScreen(
    onBack: () -> Unit,
    onContribute: () -> Unit,
    onRunInBackground: () -> Unit,
    onLanguage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            LargeFlexibleTopAppBar(
                title = { Text(stringResource(Res.string.headline_settings)) },
                navigationIcon = { ArrowBackIconButton(onBack) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = paddingValues.add(vertical = 8.dp),
        ) {
            item {
                PrimaryAction(
                    onContribute = onContribute,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                )
            }
            item { Spacer(Modifier.height(16.dp)) }
            item {
                ListItem(
                    headlineContent = {
                        Text(stringResource(Res.string.headline_run_in_background))
                    },
                    modifier = Modifier.heightIn(min = 68.dp).clickable { onRunInBackground() },
                    leadingContent = { Icon(Icons.Outlined.Engineering, null) },
                    supportingContent = {
                        Text(stringResource(Res.string.description_run_in_background))
                    },
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(stringResource(Res.string.headline_language)) },
                    modifier = Modifier.heightIn(min = 68.dp).clickable { onLanguage() },
                    leadingContent = { Icon(Icons.Outlined.Language, null) },
                    supportingContent = { Text(currentLanguage()) },
                )
            }
        }
    }
}

@Composable internal expect fun currentLanguage(): String

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PrimaryAction(
    onContribute: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.padding(contentPadding), contentAlignment = Alignment.Center) {
        Button(
            onClick = onContribute,
            shapes = ButtonDefaults.shapesFor(ButtonDefaults.LargeContainerHeight),
            modifier = Modifier.fillMaxWidth().heightIn(min = ButtonDefaults.LargeContainerHeight),
            contentPadding = ButtonDefaults.LargeContentPadding,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.VolunteerActivism,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.LargeIconSize),
                )
                Spacer(Modifier.width(ButtonDefaults.LargeIconSpacing))
                Column {
                    Text(
                        text = stringResource(Res.string.headline_contribute),
                        style = MaterialTheme.typography.titleLargeEmphasized,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = stringResource(Res.string.description_contribute),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}
