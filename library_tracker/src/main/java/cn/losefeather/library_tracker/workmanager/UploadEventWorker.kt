package cn.losefeather.library_tracker.workmanager

import android.content.Context
import androidx.room.Room
import androidx.work.WorkerParameters
import androidx.work.multiprocess.RemoteCoroutineWorker
import cn.losefeather.library_tracker.database.EventTrackerDataBase
import cn.losefeather.library_tracker.entity.EventInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UploadEventWorker(appContext: Context, workerParams: WorkerParameters) :
    RemoteCoroutineWorker(appContext, workerParams) {

    private val dataBase: EventTrackerDataBase = Room.databaseBuilder(
        appContext,
        EventTrackerDataBase::class.java, "tracker_database"
    ).build()


    override suspend fun doRemoteWork(): Result {
        withContext(Dispatchers.IO) {
            val eventList = queryNotUploadEvent()
            if (eventList.isEmpty()) {
                return@withContext Result.success()
            } else {
                withContext(Dispatchers.Default) {
                    uploadEvents(eventList)
                }
                deleteNotUploadEvent(eventList)
                return@withContext Result.success()
            }
        }
        // Indicate whether the work finished successfully with the Result
        return Result.failure()
    }

    private suspend fun uploadEvents(list: List<EventInfo>) {

    }


    private suspend fun queryNotUploadEvent(): List<EventInfo> {
        return dataBase.eventDao().queryNotUploadEvent()
    }


    private suspend fun deleteNotUploadEvent(list: List<EventInfo>) {
        dataBase.eventDao().delete(*list.toTypedArray())
    }

}