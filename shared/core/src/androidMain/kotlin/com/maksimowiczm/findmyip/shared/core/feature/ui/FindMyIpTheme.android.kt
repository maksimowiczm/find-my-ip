package com.maksimowiczm.findmyip.shared.core.feature.ui

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun FindMyIpTheme(content: @Composable (() -> Unit)) {
    FindMyIpTheme(dynamicColor = true, content = content)
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun FindMyIpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable (() -> Unit),
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> DefaultTheme.DarkColorScheme
            else -> DefaultTheme.LightColorScheme
        }

    MaterialExpressiveTheme(colorScheme = colorScheme, content = content)
}

private object DefaultTheme {

    private val Purple80 = Color(0xFFD0BCFF)
    private val PurpleGrey80 = Color(0xFFCCC2DC)
    private val Pink80 = Color(0xFFEFB8C8)

    private val Purple40 = Color(0xFF6650a4)
    private val PurpleGrey40 = Color(0xFF625b71)
    private val Pink40 = Color(0xFF7D5260)

    val DarkColorScheme =
        darkColorScheme(primary = Purple80, secondary = PurpleGrey80, tertiary = Pink80)

    val LightColorScheme =
        lightColorScheme(primary = Purple40, secondary = PurpleGrey40, tertiary = Pink40)
}
