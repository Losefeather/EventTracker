package cn.losefeather.library_tracker.entity

import androidx.room.Entity

@Entity
data class AppInfo(

    private val packName: String,
    private val appVersion: String,
    private val appVersionCode: String
)
