package cn.losefeather.library_tracker.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cn.losefeather.library_tracker.database.DataBaseContact.TABLE.TABLE_EVENT_INFO
import cn.losefeather.library_tracker.database.HashMapConverter
import cn.losefeather.library_tracker.entity.EVENT.EVENT_CATEGORY
import cn.losefeather.library_tracker.entity.EVENT.EVENT_NAME
import cn.losefeather.library_tracker.entity.EVENT.EVENT_PROP
import cn.losefeather.library_tracker.entity.EVENT.EVENT_TIME
import cn.losefeather.library_tracker.entity.EVENT.EVENT_UPLOAD_STATUS

@Entity(tableName = TABLE_EVENT_INFO)
@TypeConverters(HashMapConverter::class)
data class EventInfo(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    @ColumnInfo(name = EVENT_NAME)
    val eventName: String,
    @ColumnInfo(name = EVENT_CATEGORY)
    val eventCategory: String,
    @ColumnInfo(name = EVENT_PROP)
    val eventProp: HashMap<String, Any> = hashMapOf(),
    @ColumnInfo(name = EVENT_TIME)
    val eventTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = EVENT_UPLOAD_STATUS)
    var eventUploadStatus: Boolean = false
)
