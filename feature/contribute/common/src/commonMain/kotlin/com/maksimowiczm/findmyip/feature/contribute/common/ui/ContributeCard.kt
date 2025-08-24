package com.maksimowiczm.findmyip.feature.contribute.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.shared.ui.FindMyIpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ContributeCard(
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    buttonLabel: @Composable () -> Unit,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                icon()
                CompositionLocalProvider(
                    LocalTextStyle provides
                        MaterialTheme.typography.headlineSmallEmphasized.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                ) {
                    title()
                }
            }
            CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyMedium) {
                description()
            }
            Button(
                onClick = onAction,
                shapes = ButtonDefaults.shapes(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                buttonLabel()
            }
        }
    }
}

@Preview
@Composable
private fun ContributeCardPreview() {
    FindMyIpTheme {
        ContributeCard(
            icon = {
                Icon(Icons.Outlined.Info, null, Modifier.size(ContributeCardDefaults.iconSize))
            },
            title = { Text("Contribute") },
            description = {
                Text(
                    "Support the project by contributing your time, skills, or resources. Every bit helps us improve and grow."
                )
            },
            buttonLabel = { Text("Learn More") },
            onAction = { /* Replace with actual action */ },
            modifier = Modifier.padding(16.dp),
        )
    }
}

object ContributeCardDefaults {
    val iconSize: Dp = 32.dp
}
