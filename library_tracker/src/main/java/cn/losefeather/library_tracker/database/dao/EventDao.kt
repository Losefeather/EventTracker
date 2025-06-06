package cn.losefeather.library_tracker.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cn.losefeather.library_tracker.database.DataBaseContact.TABLE.TABLE_EVENT_INFO
import cn.losefeather.library_tracker.entity.EventInfo

@Dao
interface EventDao {
    @Query("select * FROM $TABLE_EVENT_INFO WHERE event_upload_status == 0")
    suspend fun queryNotUploadEvent(): List<EventInfo>

    @Insert
    suspend fun insertAll(vararg eventInfos: EventInfo)

    @Delete(entity = EventInfo::class)
    suspend fun delete(vararg eventInfos: EventInfo)
}