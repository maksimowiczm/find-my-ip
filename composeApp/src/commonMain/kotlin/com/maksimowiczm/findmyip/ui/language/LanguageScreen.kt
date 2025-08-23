package com.maksimowiczm.findmyip.ui.language

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.shared.ui.ArrowBackIconButton
import com.maksimowiczm.findmyip.shared.ui.FindMyIpTheme
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.action_translate
import findmyip.composeapp.generated.resources.description_translate_short
import findmyip.composeapp.generated.resources.headline_language
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
expect fun LanguageScreen(
    onBack: () -> Unit,
    onTranslate: () -> Unit,
    modifier: Modifier = Modifier,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LanguageScreen(
    currentTag: String?,
    onUpdateLanguage: (tag: String?) -> Unit,
    onBack: () -> Unit,
    onTranslate: () -> Unit,
    modifier: Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.headline_language)) },
                navigationIcon = { ArrowBackIconButton(onBack) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = paddingValues.add(vertical = 8.dp),
        ) {
            item {
                TranslateButton(
                    onClick = onTranslate,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            item { Spacer(Modifier.heightIn(16.dp)) }
            item {
                ListItem(
                    headlineContent = { Text("System") },
                    modifier = Modifier.clickable { onUpdateLanguage(null) },
                    leadingContent = { RadioButton(selected = currentTag == null, onClick = null) },
                )
            }
            items(languages) { (tag, name, authors) ->
                ListItem(
                    headlineContent = { Text(name) },
                    modifier = Modifier.clickable { onUpdateLanguage(tag) },
                    leadingContent = { RadioButton(selected = tag == currentTag, onClick = null) },
                    supportingContent = {
                        Column { authors.forEach { Text(it.toAnnotatedString()) } }
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TranslateButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shapes = ButtonDefaults.shapesFor(ButtonDefaults.LargeContainerHeight),
        modifier = modifier.fillMaxWidth().heightIn(min = ButtonDefaults.LargeContainerHeight),
        contentPadding = ButtonDefaults.LargeContentPadding,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Translate,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.LargeIconSize),
            )
            Spacer(Modifier.width(ButtonDefaults.LargeIconSpacing))
            Column {
                Text(
                    text = stringResource(Res.string.action_translate),
                    style = MaterialTheme.typography.titleLargeEmphasized,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = stringResource(Res.string.description_translate_short),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Preview
@Composable
private fun LanguageScreenPreview() {
    FindMyIpTheme { LanguageScreen(onBack = {}, onTranslate = {}) }
}
