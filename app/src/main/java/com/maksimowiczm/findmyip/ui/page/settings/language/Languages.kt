package com.maksimowiczm.findmyip.ui.page.settings.language

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle

private val me = Author(
    name = "Mateusz Maksimowicz",
    link = "https://github.com/maksimowiczm"
)

val languages = mapOf(
    "English (United States)" to Translation(
        tag = "en-US",
        authors = listOf(me)
    ),
    "Polski (Polska)" to Translation(
        tag = "pl-PL",
        authors = listOf(me)
    )
)

fun Map<String, Translation>.containsTag(tag: String): Boolean = this.values.any { it.tag == tag }

data class Translation(val tag: String, val authors: List<Author>)

data class Author(val name: String, val link: String? = null) {
    @Composable
    fun toAnnotatedString(): AnnotatedString = if (link != null) {
        val linkStyle = MaterialTheme.colorScheme.primary
        val textStyle = LocalTextStyle.current
        val spanStyle = textStyle.merge(linkStyle).toSpanStyle()

        remember(this, linkStyle, textStyle) {
            buildAnnotatedString {
                withStyle(
                    style = spanStyle.copy(
                        fontStyle = FontStyle.Italic
                    )
                ) {
                    withLink(
                        LinkAnnotation.Url(
                            url = link
                        )
                    ) {
                        append(name)
                    }
                }
            }
        }
    } else {
        remember(this) {
            AnnotatedString(name)
        }
    }
}
