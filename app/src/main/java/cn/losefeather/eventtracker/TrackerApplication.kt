package cn.losefeather.eventtracker

import android.app.Application
import cn.losefeather.library_tracker.EventTrackerManager

class TrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        EventTrackerManager.getInstance().init(this)
    }
}