@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2

import android.content.Context
import com.maksimowiczm.findmyip._2.data.IsMigratedFrom1
import java.io.File

val Context.isMigratedFrom1: IsMigratedFrom1
    get() = IsMigratedFrom1 { File(filesDir, "datastore/settings.preferences_pb").exists() }
