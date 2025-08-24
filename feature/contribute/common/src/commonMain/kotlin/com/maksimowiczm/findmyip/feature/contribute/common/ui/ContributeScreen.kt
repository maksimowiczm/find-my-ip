package com.maksimowiczm.findmyip.feature.contribute.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.shared.ui.ArrowBackIconButton
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.description_contribute
import findmyip.composeapp.generated.resources.headline_contribute
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ContributeScreen(
    onBack: () -> Unit,
    actions: List<ContributeAction>,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.headline_contribute)) },
                navigationIcon = { ArrowBackIconButton(onBack) },
                subtitle = { Text(stringResource(Res.string.description_contribute)) },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                    ),
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(360.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = paddingValues.add(horizontal = 16.dp, vertical = 8.dp),
        ) {
            items(items = actions) { action ->
                ContributeCard(
                    icon = {
                        Icon(action.icon, null, Modifier.size(ContributeCardDefaults.iconSize))
                    },
                    title = { Text(stringResource(action.title)) },
                    description = { Text(stringResource(action.description)) },
                    buttonLabel = { Text(stringResource(action.buttonLabel)) },
                    onAction = action.action,
                )
            }
        }
    }
}
