package cn.losefeather.library_tracker.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cn.losefeather.library_tracker.database.DataBaseContact.TABLE.TABLE_USER_INFO


@Entity(tableName = TABLE_USER_INFO)
data class UserInfo(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "user_id") val userId: String,
)


