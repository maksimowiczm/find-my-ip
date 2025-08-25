package com.maksimowiczm.findmyip.application.infrastructure

import com.maksimowiczm.findmyip.shared.core.application.infrastructure.config.AppConfig

interface OpensourceAppConfig : AppConfig {
    val featureRequestUrl: String
    val bugReportUrl: String
}
