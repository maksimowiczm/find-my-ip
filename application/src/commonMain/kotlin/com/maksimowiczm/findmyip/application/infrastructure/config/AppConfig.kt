package com.maksimowiczm.findmyip.application.infrastructure.config

interface AppConfig {
    val appUrl: String
    val featureRequestUrl: String
    val bugReportUrl: String
    val translateUrl: String
    val feedbackEmailUri: String
}
