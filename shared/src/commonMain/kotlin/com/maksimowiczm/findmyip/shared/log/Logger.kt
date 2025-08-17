package com.maksimowiczm.findmyip.shared.log

interface Logger {
    fun d(tag: String, throwable: Throwable? = null, message: () -> String)

    fun d(tag: String, message: () -> String) = d(tag, null, message)

    fun w(tag: String, throwable: Throwable? = null, message: () -> String)

    fun w(tag: String, message: () -> String) = w(tag, null, message)

    fun e(tag: String, throwable: Throwable? = null, message: () -> String)

    fun e(tag: String, message: () -> String) = e(tag, null, message)

    fun i(tag: String, throwable: Throwable? = null, message: () -> String)

    fun i(tag: String, message: () -> String) = i(tag, null, message)
}

internal expect object FindMyIpLogger : Logger {
    override fun d(tag: String, throwable: Throwable?, message: () -> String)

    override fun w(tag: String, throwable: Throwable?, message: () -> String)

    override fun e(tag: String, throwable: Throwable?, message: () -> String)

    override fun i(tag: String, throwable: Throwable?, message: () -> String)
}
