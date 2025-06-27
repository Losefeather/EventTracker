package cn.losefeather.library_tracker

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import cn.losefeather.library_tracker.entity.EVENT.EVENT_PAGE_HIDDEN
import cn.losefeather.library_tracker.entity.EVENT.EVENT_PAGE_SHOW
import cn.losefeather.library_tracker.entity.KEY.PAGE_NAME
import cn.losefeather.library_tracker.entity.KEY.PAGE_TYPE

class EventTrackerActivityLifecycle constructor(
    val trackActivity: Boolean,
    val trackFragment: Boolean
) :
    Application.ActivityLifecycleCallbacks {

    // AndroidX Fragment监听（优先支持）
    private val androidxFragmentLifecycle by lazy {
        AndroidXFragmentLifecycle()
    }

    // 原生Fragment监听（兼容旧版本）
    @RequiresApi(Build.VERSION_CODES.O)
    private val nativeFragmentLifecycle = NativeFragmentLifecycle()


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (trackFragment) {
            if (activity is FragmentActivity) {
                activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                    androidxFragmentLifecycle,
                    true
                )
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // API 26+支持原生回调
                    activity.fragmentManager.registerFragmentLifecycleCallbacks(
                        nativeFragmentLifecycle,
                        true
                    )
                }
            }
        }
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        if (trackActivity) {
            EventTrackerManager.getInstance().trackUiEvent(
                EVENT_PAGE_SHOW,
                hashMapOf(PAGE_NAME to activity::class.java.simpleName, PAGE_TYPE to "ACTIVITY")
            )
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (trackActivity) {
            EventTrackerManager.getInstance().trackUiEvent(
                EVENT_PAGE_HIDDEN,
                hashMapOf(PAGE_NAME to activity::class.java.simpleName, PAGE_TYPE to "ACTIVITY")
            )
        }
    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        if (trackFragment) {
            if (activity is FragmentActivity) {
                activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(
                    androidxFragmentLifecycle
                )
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // API 26+支持原生回调
                    activity.fragmentManager.unregisterFragmentLifecycleCallbacks(
                        nativeFragmentLifecycle,
                    )
                }
            }
        }
    }
}