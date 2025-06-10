package cn.losefeather.library_tracker.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cn.losefeather.library_tracker.database.DataBaseContact.TABLE.TABLE_EVENT_INFO
import cn.losefeather.library_tracker.entity.EventInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("select * FROM $TABLE_EVENT_INFO WHERE event_upload_status == 0")
    suspend fun queryNotUploadEvents(): Flow<List<EventInfo>>

    @Insert
    suspend fun insertAll(eventInfos: List<EventInfo>): Flow<List<Long>>

    @Insert
    suspend fun insert(eventInfos: EventInfo): Flow<Long>

    @Delete(entity = EventInfo::class)
    suspend fun delete(vararg eventInfos: EventInfo): Flow<Int>
}