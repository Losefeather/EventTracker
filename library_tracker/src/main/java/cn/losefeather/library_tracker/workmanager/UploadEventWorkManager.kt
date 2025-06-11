package cn.losefeather.library_tracker.workmanager

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkRequest

class UploadEventWorkManager {
    companion object {
        val uploadWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<UploadEventWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()

        fun scheduleUploadEvent(context: Context) {
            WorkManager.getInstance(context).enqueue(uploadWorkRequest)
        }
    }
}