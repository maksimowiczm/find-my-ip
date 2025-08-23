package com.maksimowiczm.findmyip.feature.language.ui

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

internal val languages =
    listOf(
        Translation(tag = "en-US", name = "English (United States)", authors = listOf()),
        Translation(
            tag = "pl-PL",
            name = "Polski (Polska)",
            authors =
                listOf(
                    Author(name = "Mateusz Maksimowicz", link = "https://github.com/maksimowiczm")
                ),
        ),
    )

internal fun List<Translation>.containsTag(tag: String): Boolean = any { it.tag == tag }

internal data class Translation(val tag: String, val name: String, val authors: List<Author>)

internal data class Author(val name: String, val link: String? = null) {
    @Composable
    fun toAnnotatedString(): AnnotatedString =
        if (link != null) {
            val linkStyle = MaterialTheme.colorScheme.primary
            val textStyle = LocalTextStyle.current
            val spanStyle = textStyle.merge(linkStyle).toSpanStyle()

            remember(this, linkStyle, textStyle) {
                buildAnnotatedString {
                    withStyle(style = spanStyle.copy(fontStyle = FontStyle.Italic)) {
                        withLink(LinkAnnotation.Url(url = link)) { append(name) }
                    }
                }
            }
        } else {
            remember(this) { AnnotatedString(name) }
        }
}
