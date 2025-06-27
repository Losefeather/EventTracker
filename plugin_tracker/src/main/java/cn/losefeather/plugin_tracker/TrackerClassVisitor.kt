package cn.losefeather.plugin_tracker

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

/**
 *
 */
class TrackerClassVisitor(apiVersion: Int, nextClassVisitor: ClassVisitor) :
    ClassVisitor(apiVersion, nextClassVisitor) {
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        println("visit class name: $name  interfaces count: ${interfaces?.size}")
        super.visit(version, access, name, signature, superName, interfaces)
    }


    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        // 拦截View的setOnClickListener方法
//        if (name == "setOnClickListener" && descriptor == "(Landroid/view/View\$OnClickListener;)V") {
//            println("拦截了onClickListener")
        println("visitMethod $name || $descriptor || $signature")
            return ViewOnClickMethodVisitor(mv, access, name, descriptor)
        //}
        //return mv
    }
}