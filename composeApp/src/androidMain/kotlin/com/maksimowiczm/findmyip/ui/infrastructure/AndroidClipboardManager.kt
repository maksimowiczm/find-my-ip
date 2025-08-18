package com.maksimowiczm.findmyip.ui.infrastructure

import android.content.ClipData
import android.content.Context
import android.os.Build
import android.widget.Toast
import com.maksimowiczm.findmyip.presentation.infrastructure.ClipboardManager

class AndroidClipboardManager(private val context: Context) : ClipboardManager {
    private val clipboard: android.content.ClipboardManager
        get() =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager

    private val copyMessage: String
        get() = "Copied to clipboard"

    override fun copyToClipboard(text: String, label: String?) {
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            Toast.makeText(context, copyMessage, Toast.LENGTH_SHORT).show()
        }
    }
}
