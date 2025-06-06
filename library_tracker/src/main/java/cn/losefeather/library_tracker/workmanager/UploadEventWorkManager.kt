package cn.losefeather.library_tracker.workmanager

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest

class UploadEventWorkManager {
    val uploadWorkRequest: WorkRequest =
        OneTimeWorkRequestBuilder<UploadEventWorker>()
            .build()

    fun scheduleUploadEvent(context: Context) {
        WorkManager.getInstance(context).enqueue(uploadWorkRequest)
    }
}