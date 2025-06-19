package cn.losefeather.plugin_tracker

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

abstract class TrackerAsmClassVisitorFactory : AsmClassVisitorFactory<TrackerParameters> {
    private var isTrackFragment = false


    private val fragmentSuperClasses = setOf(
        "androidx/fragment/app/Fragment",  // AndroidX Fragment
        "android/app/Fragment"             // 原生 Fragment
    )


    override fun isInstrumentable(classData: ClassData): Boolean {
        val excludePackages = parameters.get().excludePackages.get()
        isTrackFragment = parameters.get().isTrackFragment.get()
        return !classData.className.startsWithAny(
            excludePackages.split(",").filter { it.isNotBlank() })
    }

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        println("打印出来 ${instrumentationContext.apiVersion}   || ${Opcodes.ASM9}")

        if (isTrackFragment && isFragment(classContext)) {
            return FragmentClassVisitor(Opcodes.ASM9, nextClassVisitor)
        }
        return TrackerClassVisitor(Opcodes.ASM9, nextClassVisitor)
    }

    // 辅助函数：判断类名是否以指定前缀开头
    private fun String.startsWithAny(prefixes: List<String>): Boolean {
        return prefixes.any { this.startsWith(it) }
    }

    private fun isFragment(classContext: ClassContext): Boolean {
        val allSuperClasses = classContext.currentClassData.superClasses
        val isFragment = allSuperClasses.any { it in fragmentSuperClasses }
        return isFragment
    }
}