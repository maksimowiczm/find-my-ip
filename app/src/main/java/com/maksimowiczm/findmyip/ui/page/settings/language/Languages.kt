package com.maksimowiczm.findmyip.ui.page.settings.language

val languages = mapOf(
    "English (United States)" to Translation(
        tag = "en-US"
    )
)

fun Map<String, Translation>.containsTag(tag: String): Boolean = this.values.any { it.tag == tag }

data class Translation(val tag: String)
