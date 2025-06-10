package cn.losefeather.library_tracker

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import cn.losefeather.library_tracker.database.TrackerDataBaseManager


class EventTracker {
    private val eventTrackerActivityLifecycle by lazy {
        EventTrackerActivityLifecycle(this)
    }

    private val eventCache by lazy { EventCache() }

    fun init(application: Application) {
        initDataBase(application)
        registerActivityLifecycle(application)
        register(application)
    }

    private fun initDataBase(application: Application) {
        TrackerDataBaseManager.getInstance().init(application)
    }

    /**
     *
     */
    private fun registerActivityLifecycle(application: Application) {
        application.registerActivityLifecycleCallbacks(eventTrackerActivityLifecycle)
    }


    private fun register(application: Application) {
        application.registerComponentCallbacks(object : ComponentCallbacks2 {
            override fun onConfigurationChanged(newConfig: Configuration) {
            }

            override fun onLowMemory() {

            }

            override fun onTrimMemory(level: Int) {
            }

        })
    }

    /**
     *
     */
    private fun unregisterActivityLifecycle(application: Application) {
        application.unregisterActivityLifecycleCallbacks(eventTrackerActivityLifecycle)
    }


    fun trackEvent() {

    }

    fun trackViewEvent() {

    }

    fun trackClickEvent() {

    }

    fun trackBusinessEvent() {

    }

    private fun saveCacheEventsToDataBase() {
        if (eventCache.getAllCacheEvent().isNotEmpty()) {
            TrackerDataBaseManager.getInstance().insertEvents(eventCache.getAllCacheEvent())
            eventCache.removeEvent()
        }
    }




}


