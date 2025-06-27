package cn.losefeather.library_tracker

import android.app.Application
import android.net.Uri
import android.view.View
import cn.losefeather.library_tracker.grpc.TrackerGrpcService

class EventTrackerManager {
    private val eventTracker by lazy {
        EventTracker()
    }
    private var isInit = false


    companion object {
        @Volatile
        private lateinit var instance: EventTrackerManager

        // 添加 @JvmStatic 注解使 Java 能识别为静态方法
        @JvmStatic
        fun getInstance(): EventTrackerManager {
            if (!::instance.isInitialized) {
                instance = EventTrackerManager()
            }
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
        TrackerGrpcService(Uri.parse("http://121.160.76.104:8893"), application)
    }


    private fun initEventTracker(application: Application) {
        eventTracker.init(application)
    }

    fun trackUiEvent(eventName: String, eventProp: HashMap<String, Any>) {
        if (isInit) {
            eventTracker.trackUiEvent(eventName, eventProp)
        }
    }

    fun wrapViewOnClick(listener: View.OnClickListener): View.OnClickListener {
        if (isInit) {
            return eventTracker.wrapViewOnClick(listener)
        }
        return listener
    }

    fun trackViewEvent(view: View) {
        if (isInit) {
            eventTracker.trackViewClick(view)
        }
    }

    fun setConfig(): EventTrackerManager {

        return this
    }

}