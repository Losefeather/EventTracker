package cn.losefeather.library_tracker

import android.app.Application
import androidx.room.Room
import cn.losefeather.library_tracker.database.EventTrackerDataBase


class EventTracker {
    private val eventTrackerActivityLifecycle by lazy {
        EventTrackerActivityLifecycle(this)
    }
    private lateinit var dataBase: EventTrackerDataBase

    fun init(application: Application) {
        initDataBase(application)
        registerActivityLifecycle(application)
    }

    private fun initDataBase(application: Application) {
        dataBase = Room.databaseBuilder(
            application,
            EventTrackerDataBase::class.java, "tracker_database"
        ).build()
    }

    /**
     *
     */
    private fun registerActivityLifecycle(application: Application) {
        application.registerActivityLifecycleCallbacks(eventTrackerActivityLifecycle)
    }

    /**
     *
     */
    private fun unregisterActivityLifecycle(application: Application) {
        application.unregisterActivityLifecycleCallbacks(eventTrackerActivityLifecycle)
    }


    fun trackEvent() {

    }
}


