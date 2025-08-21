package com.maksimowiczm.findmyip.presentation.infrastructure

interface ClipboardManager {
    fun copyToClipboard(text: String, label: String? = null)

    companion object {
        val noop: ClipboardManager =
            object : ClipboardManager {
                override fun copyToClipboard(text: String, label: String?) = Unit
            }
    }
}
