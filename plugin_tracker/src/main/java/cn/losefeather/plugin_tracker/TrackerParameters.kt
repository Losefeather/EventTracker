package cn.losefeather.plugin_tracker

import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

interface TrackerParameters : InstrumentationParameters {
    // 可自定义插桩参数（如需要过滤的包名）
    @get:Input
    val excludePackages: Property<String>

    @get:Input
    val trackFragmentEnabled: Property<Boolean>
//
//    @get:Input
//    val autoTrackClick: Property<Boolean>
//
//    @get:Input
//    val autoTrackPage: Property<Boolean>
}