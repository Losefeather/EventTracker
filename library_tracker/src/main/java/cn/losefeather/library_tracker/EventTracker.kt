package cn.losefeather.library_tracker

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.util.Log
import android.view.View
import cn.losefeather.library_tracker.database.TrackerDataBaseManager
import cn.losefeather.library_tracker.entity.CATEGORY.CATEGORY_BUSINESS
import cn.losefeather.library_tracker.entity.CATEGORY.CATEGORY_UI
import cn.losefeather.library_tracker.entity.EVENT.EVENT_ON_CLICK
import cn.losefeather.library_tracker.entity.EventInfo
import cn.losefeather.library_tracker.entity.KEY.VIEW_ID
import cn.losefeather.library_tracker.entity.KEY.VIEW_TYPE


class EventTracker {
    private var isInit = false
    private val eventTrackerActivityLifecycle by lazy {
        EventTrackerActivityLifecycle(true, true)
    }

    private val TAG = this::class.java.canonicalName

    private val eventCache by lazy { EventCache() }

    fun init(application: Application) {
        if (!isInit) {
            initDataBase(application)
            registerActivityLifecycle(application)
            registerLowMemory(application)
            isInit = true
        }
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


    private fun registerLowMemory(application: Application) {
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

    fun wrapViewOnClick(listener: View.OnClickListener): View.OnClickListener {
        return View.OnClickListener { v ->
            // 执行原始点击逻辑
            listener.onClick(v)
            // 埋点逻辑：记录 View ID、类名、点击时间等
            trackViewClick(v)
        }
    }

    fun trackViewClick(id: String, type: String) {
        val hashMap = hashMapOf<String, Any>()
        try {
            hashMap[VIEW_ID] = id
            hashMap[VIEW_TYPE] = type
        } catch (e: Exception) {
            e.printStackTrace()
        }
        trackUiEvent(EVENT_ON_CLICK, hashMap)
    }

    fun trackViewClick(view: View) {
        trackViewClick(view.context.resources.getResourceEntryName(view.id), view.getViewType())
    }

    private fun View.getViewType(): String {
        return this::class.java.simpleName
    }

    fun trackUiEvent(eventName: String, eventProp: HashMap<String, Any> = hashMapOf()) {
        val eventInfo = EventInfo(
            uid = 1,
            eventName = eventName,
            eventCategory = CATEGORY_UI,
            eventProp = eventProp,
            eventUploadStatus = false
        )
        Log.e(TAG, "trackUiEvent: $eventInfo")
        eventCache.addEvent(eventInfo)
    }

    fun trackBusinessEvent(eventName: String, eventProp: HashMap<String, Any> = hashMapOf()) {
        val eventInfo = EventInfo(
            uid = 1,
            eventName = eventName,
            eventCategory = CATEGORY_BUSINESS,
            eventProp = eventProp,
            eventUploadStatus = false
        )
        eventCache.addEvent(eventInfo)
    }

    private fun saveCacheEventsToDataBase() {
        if (eventCache.getAllCacheEvent().isNotEmpty()) {
            TrackerDataBaseManager.getInstance().insertEvents(eventCache.getAllCacheEvent())
        }
    }
}


