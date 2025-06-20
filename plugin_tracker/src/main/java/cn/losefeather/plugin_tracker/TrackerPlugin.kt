package cn.losefeather.plugin_tracker

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

import org.gradle.api.provider.Property // 导入 Gradle 的 Property 接口

class TrackerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("注册ui隐藏埋点插件")
//        val trackerExtension = project.extensions.create("tracker", TrackerExtension::class.java)
//        println("${trackerExtension.isTrackFragment}")

        println("注册ui隐藏埋点插件2")

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
                params.isTrackFragment1.set(true)
                println("注册ui隐藏埋点插件3")

            }
            // 确保资源不被混淆
            variant.instrumentation.setAsmFramesComputationMode(
                FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }
    }
}


// 插件扩展类（定义可配置参数）
interface TrackerExtension {
    val isTrackFragment: Property<Boolean>
    val excludePackages: Property<String>
}