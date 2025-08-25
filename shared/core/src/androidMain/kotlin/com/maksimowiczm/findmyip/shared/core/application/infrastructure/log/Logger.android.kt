package com.maksimowiczm.findmyip.shared.core.application.infrastructure.log

import android.util.Log

internal object FindMyIpLogger : Logger {
    override fun d(tag: String, throwable: Throwable?, message: () -> String) {
        Log.d(tag, message(), throwable)
    }

    override fun w(tag: String, throwable: Throwable?, message: () -> String) {
        Log.w(tag, message(), throwable)
    }

    override fun e(tag: String, throwable: Throwable?, message: () -> String) {
        Log.e(tag, message(), throwable)
    }

    override fun i(tag: String, throwable: Throwable?, message: () -> String) {
        Log.i(tag, message(), throwable)
    }
}
