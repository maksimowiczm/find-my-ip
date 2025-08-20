package com.maksimowiczm.findmyip.infrastructure

import FindmyIP.composeApp.BuildConfig
import com.maksimowiczm.findmyip.application.infrastructure.config.AppConfig

class FindMyIpConfig : AppConfig {
    override val featureRequestUrl: String = BuildConfig.GITHUB_ISSUES_URL
    override val bugReportUrl: String = BuildConfig.GITHUB_ISSUES_URL
    override val translateUrl: String = BuildConfig.CROWDIN_URL
    override val emailUri: String = BuildConfig.FEEDBACK_EMAIL_URI
}
