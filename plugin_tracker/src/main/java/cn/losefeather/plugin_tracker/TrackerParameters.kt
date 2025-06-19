package cn.losefeather.plugin_tracker

import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

abstract class TrackerParameters : InstrumentationParameters {
    // 可自定义插桩参数（如需要过滤的包名）
    abstract val excludePackages: Property<String>
    abstract val isTrackFragment: Property<Boolean>


    fun setDefaults(objects: ObjectFactory) {
        excludePackages.convention("")
    }
}