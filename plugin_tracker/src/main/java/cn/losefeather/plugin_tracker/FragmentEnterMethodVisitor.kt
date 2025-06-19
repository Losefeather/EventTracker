package cn.losefeather.plugin_tracker

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class FragmentEnterMethodVisitor(
    mv: MethodVisitor,
    apiVersion: Int,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(
    apiVersion, mv, access, name, descriptor
) {
    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
        // 插入埋点代码
        insertTrackCode()
    }

    private fun insertTrackCode() {
        // 获取View ID
        mv.visitVarInsn(Opcodes.ALOAD, 0) // this (View对象)
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "android/view/View",
            "getId",
            "()I",
            false
        )
        mv.visitVarInsn(Opcodes.ISTORE, 2) // 存储viewId到局部变量2

        // 调用EventTracker.trackUiEvent()
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "cn/losefeather/library_tracker/EventTracker",
            "getInstance",
            "()Lcn/losefeather/library_tracker/EventTracker;",
            false
        )

        // 事件名称：从EventContact.CLICK_EVENT获取
        mv.visitLdcInsn("CLICK_EVENT") // 需确保与EventContact.kt中定义一致

        // View ID参数
        mv.visitVarInsn(Opcodes.ILOAD, 2)

        // 创建空HashMap作为eventProp
        mv.visitTypeInsn(Opcodes.NEW, "java/util/HashMap")
        mv.visitInsn(Opcodes.DUP)
        mv.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "java/util/HashMap",
            "<init>",
            "()V",
            false
        )

        // 调用trackUiEvent方法
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "cn/losefeather/library_tracker/EventTracker",
            "trackUiEvent",
            "(Ljava/lang/String;ILjava/util/HashMap;)V",
            false
        )
    }
}