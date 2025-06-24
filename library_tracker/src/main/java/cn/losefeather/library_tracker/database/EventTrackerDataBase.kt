package cn.losefeather.library_tracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cn.losefeather.library_tracker.database.dao.EventDao
import cn.losefeather.library_tracker.entity.EventInfo
import cn.losefeather.library_tracker.entity.UserInfo

@Database(entities = [UserInfo::class, EventInfo::class], version = 1, exportSchema = false)
@TypeConverters(HashMapConverter::class)

abstract class EventTrackerDataBase : RoomDatabase() {
//    abstract fun userDao(): UserDao

    abstract fun eventDao(): EventDao
}