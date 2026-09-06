package com.maksimowiczm.findmyip.shared.feature.home.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.shared.core.feature.ui.FindMyIpTheme
import com.maksimowiczm.findmyip.shared.core.feature.ui.LocalDateFormatter
import com.maksimowiczm.findmyip.shared.feature.home.persentation.AddressUiModel
import com.maksimowiczm.findmyip.shared.feature.home.persentation.InternetProtocolVersion
import com.maksimowiczm.findmyip.shared.feature.home.persentation.NetworkType
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.now
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun AddressButton(
    model: AddressUiModel,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    shapes: ButtonShapes = AddressButtonDefaults.singleShapes(),
) {
    val dateFormatter = LocalDateFormatter.current

    val timeTransition = updateTransition(model.dateTime)

    Button(
        onClick = onClick,
        modifier = modifier,
        shapes = shapes,
        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 16.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                timeTransition.AnimatedContent(
                    contentKey = { it.toString() },
                    transitionSpec = {
                        fadeIn(tween(durationMillis = 1_000)) togetherWith
                            fadeOut(tween(durationMillis = 200))
                    },
                ) {
                    Text(
                        text = dateFormatter.formatDateTimeLong(model.dateTime),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Spacer(Modifier.weight(1f))
                model.networkType.Icon()
                Spacer(Modifier.width(8.dp))
                Text(
                    text = model.networkType.stringResource(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Text(
                text = model.address,
                style = MaterialTheme.typography.headlineMediumEmphasized,
                fontWeight = FontWeight.Bold,
            )
            model.domain?.let { domain ->
                Text(
                    text = domain,
                    style = MaterialTheme.typography.bodyMediumEmphasized,
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}

object AddressButtonDefaults {

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    private val pressedShape: Shape
        @Composable get() = MaterialTheme.shapes.extraExtraLarge

    private val middleCornerRadius: CornerSize
        @Composable get() = MaterialTheme.shapes.medium.bottomEnd

    private val outerCornerRadius: CornerSize
        @Composable get() = MaterialTheme.shapes.extraLarge.topStart

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun topShapes(): ButtonShapes =
        ButtonDefaults.shapes(
            shape =
                RoundedCornerShape(
                    outerCornerRadius,
                    outerCornerRadius,
                    middleCornerRadius,
                    middleCornerRadius,
                ),
            pressedShape = pressedShape,
        )

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun middleShapes(): ButtonShapes =
        ButtonDefaults.shapes(
            shape = RoundedCornerShape(middleCornerRadius),
            pressedShape = pressedShape,
        )

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun bottomShapes(): ButtonShapes =
        ButtonDefaults.shapes(
            shape =
                RoundedCornerShape(
                    middleCornerRadius,
                    middleCornerRadius,
                    outerCornerRadius,
                    outerCornerRadius,
                ),
            pressedShape = pressedShape,
        )

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun singleShapes(): ButtonShapes =
        ButtonDefaults.shapes(
            shape = RoundedCornerShape(outerCornerRadius),
            pressedShape = pressedShape,
        )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
private fun AddressButtonPreview() {
    val model =
        object : AddressUiModel {
            override val internetProtocolVersion: InternetProtocolVersion =
                InternetProtocolVersion.IPV4
            override val address: String = "8.8.8.8"
            override val domain: String? = "google.com"
            override val dateTime: LocalDateTime = LocalDateTime.now()
            override val networkType: NetworkType = NetworkType.WIFI
        }

    FindMyIpTheme {
        AddressButton(
            model = model,
            onClick = {},
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}
