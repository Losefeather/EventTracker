package cn.losefeather.plugin_tracker

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

abstract class TrackerAsmClassVisitorFactory : AsmClassVisitorFactory<TrackerParameters> {
    // 改为伴生对象静态属性（避免实例状态）
    companion object {
        private val fragmentSuperClasses = setOf(
            "androidx/fragment/app/Fragment",  // AndroidX Fragment
            "android/app/Fragment"             // 原生 Fragment
        )
        private val viewSuperClasses = setOf(
            "android/view/View"
        )
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        val excludePackages = parameters.get().excludePackages.get()
        return !classData.className.startsWithAny(
            excludePackages.split(",").filter { it.isNotBlank() })
    }

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val isTrackFragment = parameters.get().trackFragmentEnabled.get()
        // 对所有类应用 TrackerClassVisitor，而非仅View子类
        return TrackerClassVisitor(Opcodes.ASM9, nextClassVisitor)

        // Fragment逻辑保持不变
//        return if (isTrackFragment && isFragment(classContext)) {
//            FragmentClassVisitor(Opcodes.ASM9, baseVisitor)
//        } else {
//            baseVisitor
//        }
    }

    private fun String.startsWithAny(prefixes: List<String>): Boolean {
        return prefixes.any { this.startsWith(it) }
    }

    private fun isView(classContext: ClassContext): Boolean {
        // 检查当前类是否是 View 的子类
        return classContext.currentClassData.superClasses.any { superClass ->
            viewSuperClasses.contains(superClass)
        }
    }

    private fun isFragment(classContext: ClassContext): Boolean {
        // 检查当前类是否是 Fragment 的子类
        return classContext.currentClassData.superClasses.any { superClass ->
            fragmentSuperClasses.contains(superClass)
        }
    }
}