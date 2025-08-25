package com.maksimowiczm.findmyip.shared.core.feature.presentation

interface ClipboardManager {
    fun copyToClipboard(text: String, label: String? = null)

    companion object {
        val noop: ClipboardManager =
            object : ClipboardManager {
                override fun copyToClipboard(text: String, label: String?) = Unit
            }
    }
}
