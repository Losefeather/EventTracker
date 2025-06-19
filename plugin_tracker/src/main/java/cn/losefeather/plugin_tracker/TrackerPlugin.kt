package cn.losefeather.plugin_tracker

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project


class TrackerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("注册ui隐藏埋点插件")
        // 获取Android组件扩展
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)

        // 注册字节码插桩
        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                TrackerAsmClassVisitorFactory::class.java,
                scope = InstrumentationScope.PROJECT
            ) { params ->
                // 可配置参数（例如排除某些包）
                params.excludePackages.set("androidx,com.google")
                params.isTrackFragment.set(true)
            }
            // 确保资源不被混淆
            variant.instrumentation.setAsmFramesComputationMode(
                FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }
    }
}