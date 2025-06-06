package cn.losefeather.library_tracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.losefeather.library_tracker.database.dao.EventDao
import cn.losefeather.library_tracker.database.dao.UserDao
import cn.losefeather.library_tracker.entity.AppInfo
import cn.losefeather.library_tracker.entity.EventInfo
import cn.losefeather.library_tracker.entity.UserInfo

@Database(entities = [UserInfo::class, AppInfo::class, EventInfo::class], version = 1)

abstract class EventTrackerDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun eventDao(): EventDao
}