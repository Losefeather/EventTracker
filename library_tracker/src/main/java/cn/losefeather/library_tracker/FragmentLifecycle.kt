package cn.losefeather.library_tracker

import android.app.Fragment
import android.app.FragmentManager
import android.os.Build
import androidx.annotation.RequiresApi
import cn.losefeather.library_tracker.entity.EVENT.EVENT_PAGE_HIDDEN
import cn.losefeather.library_tracker.entity.EVENT.EVENT_PAGE_SHOW
import cn.losefeather.library_tracker.entity.KEY.PAGE_NAME
import cn.losefeather.library_tracker.entity.KEY.PAGE_TYPE

class AndroidXFragmentLifecycle :
    androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks() {

    private var ignoreList: Array<String> = arrayOf("androidx.lifecycle.ReportFragment")
    override fun onFragmentResumed(
        fm: androidx.fragment.app.FragmentManager,
        f: androidx.fragment.app.Fragment
    ) {
        super.onFragmentResumed(fm, f)
        if (!ignoreList.contains(f.javaClass.name)) {
            trackFragmentShow(f.javaClass.name, "FRAGMENT")
        }
    }

    override fun onFragmentPaused(
        fm: androidx.fragment.app.FragmentManager,
        f: androidx.fragment.app.Fragment
    ) {
        super.onFragmentPaused(fm, f)
        if (!ignoreList.contains(f.javaClass.name)) {
            trackFragmentHide(f.javaClass.name, "FRAGMENT")
        }
    }

    private fun trackFragmentShow(name: String, type: String) {
        EventTrackerManager.getInstance().trackUiEvent(
            EVENT_PAGE_SHOW, hashMapOf(
                PAGE_NAME to name,
                PAGE_TYPE to type,
            )
        )
    }

    private fun trackFragmentHide(name: String, type: String) {
        EventTrackerManager.getInstance().trackUiEvent(
            EVENT_PAGE_HIDDEN, hashMapOf(
                PAGE_NAME to name,
                PAGE_TYPE to type,
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Deprecated("Deprecated in Java")
@Suppress("DEPRECATION") // 原生Fragment已弃用，仅做兼容
class NativeFragmentLifecycle(
) : FragmentManager.FragmentLifecycleCallbacks() {
    @Deprecated("Deprecated in Java")
    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        super.onFragmentResumed(fm, f)
        trackFragmentShow(f.javaClass.name, "FRAGMENT")
    }

    @Deprecated("Deprecated in Java")
    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        super.onFragmentPaused(fm, f)
        trackFragmentHide(f.javaClass.name, "FRAGMENT")
    }

    private fun trackFragmentShow(name: String, type: String) {
        EventTrackerManager.getInstance().trackUiEvent(
            EVENT_PAGE_SHOW, hashMapOf(
                PAGE_NAME to name,
                PAGE_TYPE to type,
            )
        )
    }

    private fun trackFragmentHide(name: String, type: String) {
        EventTrackerManager.getInstance().trackUiEvent(
            EVENT_PAGE_HIDDEN, hashMapOf(
                PAGE_NAME to name,
                PAGE_TYPE to type,
            )
        )
    }
}