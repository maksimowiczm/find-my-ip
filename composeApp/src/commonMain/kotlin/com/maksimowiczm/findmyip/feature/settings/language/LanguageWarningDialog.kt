package com.maksimowiczm.findmyip.feature.settings.language

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import findmyip.composeapp.generated.resources.*
import findmyip.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LanguageWarningDialog(onDismissRequest: () -> Unit, modifier: Modifier = Modifier) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(stringResource(Res.string.positive_ok))
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Translate,
                contentDescription = null
            )
        },
        text = {
            Text(stringResource(Res.string.description_translation_warning))
        }
    )
}
