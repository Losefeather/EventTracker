package cn.losefeather.library_tracker

import android.app.Application
import android.net.Uri
import cn.losefeather.library_tracker.grpc.TrackerGrpcService

class EventTrackerManager {
    private val eventTracker by lazy {
        EventTracker()
    }
    private var isInit = false


    companion object {
        @Volatile
        private lateinit var instance: EventTrackerManager

        fun getInstance(): EventTrackerManager {
            if (::instance.isInitialized) {
                return instance
            }
            instance = EventTrackerManager()
            return instance
        }
    }


    fun init(application: Application) {
        if (!isInit) {
            initEventTracker(application)
            initGrpcService(application)
            isInit = true
        }
    }

    private fun initGrpcService(application: Application) {
        TrackerGrpcService(Uri.parse("http://111.160.76.104:8893"), application)
    }


    private fun initEventTracker(application: Application) {
        eventTracker.init(application)
    }

    private fun trackEvent() {
        if (isInit) {
            eventTracker.trackEvent()
        }
    }
}