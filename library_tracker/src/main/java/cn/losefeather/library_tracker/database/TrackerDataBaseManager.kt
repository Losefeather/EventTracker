package cn.losefeather.library_tracker.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import cn.losefeather.library_tracker.entity.EventInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TrackerDataBaseManager private constructor() {
    private lateinit var dataBase: EventTrackerDataBase
    private var init = false
    private val dbScope by lazy { CoroutineScope(Dispatchers.IO + SupervisorJob()) }

    companion object {
        private lateinit var trackerDataBaseManager: TrackerDataBaseManager

        fun getInstance(): TrackerDataBaseManager {
            if (!this::trackerDataBaseManager.isInitialized) {
                trackerDataBaseManager = TrackerDataBaseManager()
            }
            return trackerDataBaseManager
        }
    }

    fun init(context: Context) {
        if (!init) {
            Log.e("", "init: dataBase")
            dataBase = Room.databaseBuilder(
                context,
                EventTrackerDataBase::class.java, "tracker_database"
            ).build()
            init = true
            Log.e("", "init: dataBase finish")

        }
    }

    fun insertEvents(eventInfos: List<EventInfo>) {
        dbScope.launch {
            dataBase.eventDao().insertAll(eventInfos).collect {
                if (it.size == eventInfos.size) {
                }
            }
        }
    }

    suspend fun queryNotUploadEvent(): Flow<List<EventInfo>> {
        return dataBase.eventDao().queryNotUploadEvents()
    }

    suspend fun deleteEvent(vararg events: EventInfo): Flow<Int> {
        return dataBase.eventDao().delete(*events)
    }

}