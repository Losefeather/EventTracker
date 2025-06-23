package cn.losefeather.plugin_tracker

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

class FragmentClassVisitor(val apiVersion: Int, nextClassVisitor: ClassVisitor) :
    ClassVisitor(apiVersion, nextClassVisitor) {

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {

        println("访问了fragment是 $name")
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
        // 使用从 Factory 传递的 isFragment 判断
        // Fragment 相关方法拦截逻辑（例如 onViewCreated）
        if (name == "onViewCreated" && descriptor == "(Landroid/view/View;Landroid/os/Bundle;)V") {
            return FragmentEnterMethodVisitor(mv, apiVersion = apiVersion, access, name, descriptor)
        }
        return mv
    }
}