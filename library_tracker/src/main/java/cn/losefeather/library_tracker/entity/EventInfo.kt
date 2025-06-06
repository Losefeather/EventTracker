package cn.losefeather.library_tracker.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cn.losefeather.library_tracker.database.DataBaseContact.TABLE.TABLE_EVENT_INFO
import cn.losefeather.library_tracker.database.HashMapConverter

@Entity(tableName = TABLE_EVENT_INFO)
data class EventInfo(
    @PrimaryKey
    val uid: Int,
    @ColumnInfo(name = "event_name")
    val eventName: String,
    @ColumnInfo(name = "event_category")
    val eventCategory: String,
    @TypeConverters(HashMapConverter::class)
    val eventProp: HashMap<String, Any>,
    @ColumnInfo(name = "event_time")
    val eventTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "event_upload_status")
    var eventUploadStatus: Boolean = false
)
