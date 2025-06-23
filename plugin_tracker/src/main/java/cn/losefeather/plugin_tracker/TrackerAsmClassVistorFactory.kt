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
        // 移除 println 调试语句（可能干扰序列化）
        val isTrackFragment = parameters.get().trackFragmentEnabled.get()
        if (isTrackFragment && isFragment(classContext)) {
            return FragmentClassVisitor(Opcodes.ASM9, nextClassVisitor)
        }
        return TrackerClassVisitor(Opcodes.ASM9, nextClassVisitor)
    }

    private fun String.startsWithAny(prefixes: List<String>): Boolean {
        return prefixes.any { this.startsWith(it) }
    }

    private fun isFragment(classContext: ClassContext): Boolean {
        val allSuperClasses = classContext.currentClassData.superClasses
        // 使用伴生对象的静态属性
        return allSuperClasses.any { it in fragmentSuperClasses }
    }
}