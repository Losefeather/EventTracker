package cn.losefeather.library_tracker.entity

import androidx.room.Entity

@Entity
data class AppInfo(

    val packName: String,
    val appVersion: String,
    val appVersionCode: String
)
