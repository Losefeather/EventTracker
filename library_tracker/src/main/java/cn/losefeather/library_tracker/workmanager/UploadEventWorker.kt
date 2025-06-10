package cn.losefeather.library_tracker.workmanager

import android.content.Context
import androidx.work.WorkerParameters
import androidx.work.multiprocess.RemoteCoroutineWorker
import cn.losefeather.library_tracker.database.TrackerDataBaseManager
import kotlinx.coroutines.flow.first

class UploadEventWorker(appContext: Context, workerParams: WorkerParameters) :
    RemoteCoroutineWorker(appContext, workerParams) {

    override suspend fun doRemoteWork(): Result {
        val eventList = TrackerDataBaseManager.getInstance().queryNotUploadEvent().first()
        if (eventList.isEmpty()) {
            return Result.success()
        }

        // 上传操作（假设是网络请求，仍在 IO 线程）
        uploadEvents(eventList)
        // 删除操作（IO 线程）
        val number =
            TrackerDataBaseManager.getInstance().deleteEvent(*eventList.toTypedArray()).first()
        if (number == eventList.size) {
            return Result.success()
        } else {
            return Result.retry()
        }
    }
}