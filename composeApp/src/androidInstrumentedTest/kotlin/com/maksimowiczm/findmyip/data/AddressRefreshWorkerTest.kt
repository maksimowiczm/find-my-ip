package com.maksimowiczm.findmyip.data

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddressRefreshWorkerTest {
    private lateinit var applicationContext: Context

    private val historyManager = object : HistoryManager {
        override suspend fun run() {
        }

        override suspend fun once() {
        }
    }

    inner class ExampleWorkerFactory : WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker = AddressRefreshWorker(appContext, workerParameters, historyManager)
    }

    @Before
    fun setup() {
        applicationContext = ApplicationProvider.getApplicationContext()
        val config = Configuration.Builder()
            .setWorkerFactory(ExampleWorkerFactory())
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(applicationContext, config)
    }

    @Test
    @Throws(Exception::class)
    fun testSimpleEchoWorker() {
        val request = OneTimeWorkRequestBuilder<AddressRefreshWorker>().build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueue(request).result.get()
        val workInfo = workManager.getWorkInfoById(request.id).get()

        assertThat(workInfo?.state, `is`(WorkInfo.State.SUCCEEDED))
    }
}
