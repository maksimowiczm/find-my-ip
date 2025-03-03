package com.maksimowiczm.findmyip

import android.content.Context
import com.maksimowiczm.findmyip.data.IsMigratedFrom1
import java.io.File

val Context.isMigratedFrom1: IsMigratedFrom1
    get() = IsMigratedFrom1 { File(filesDir, "datastore/settings.preferences_pb").exists() }
