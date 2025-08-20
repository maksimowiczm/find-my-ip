package com.maksimowiczm.findmyip.application.infrastructure.config

interface AppConfig {
    val featureRequestUrl: String
    val bugReportUrl: String
    val translateUrl: String
    val emailUri: String
}
