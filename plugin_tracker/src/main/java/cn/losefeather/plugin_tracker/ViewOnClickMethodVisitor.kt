package cn.losefeather.plugin_tracker

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class ViewOnClickMethodVisitor(mv: MethodVisitor, access: Int, name: String?, descriptor: String?) :
    AdviceAdapter(Opcodes.ASM9, mv, access, name, descriptor) {
//    override fun onMethodExit(opcode: Int) {
//        // 插入埋点代码
//        insertTrackCode()
//
//        super.onMethodExit(opcode)
//    }


    override fun visitMethodInsn(
        opcode: Int,
        owner: String,
        name: String,
        desc: String,
        itf: Boolean
    ) {

        println("owner $owner name $name desc $desc")
        // 修改条件：匹配所有View子类的setOnClickListener调用
        if (name == "setOnClickListener" && desc == "(Landroid/view/View\$OnClickListener;)V") {
            println("找到了埋点事件: owner=$owner")

            // 1. 获取 EventTrackerManager 实例
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "cn/losefeather/library_tracker/EventTrackerManager",
                "getInstance",
                "()Lcn/losefeather/library_tracker/EventTrackerManager;",
                false
            )

            // 2. 将原始的 OnClickListener 参数从操作数栈加载回来
            mv.visitInsn(Opcodes.SWAP)

            // 3. 调用 EventTrackerManager 的 wrapViewOnClick 方法
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "cn/losefeather/library_tracker/EventTrackerManager",
                "wrapViewOnClick",
                "(Landroid/view/View\$OnClickListener;)Landroid/view/View\$OnClickListener;",
                false
            )

            // 4. 调用原始的 setOnClickListener 方法，使用实际的 owner（如 TextView）
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                owner,  // 使用实际的 owner（如 android/widget/TextView）
                "setOnClickListener",
                "(Landroid/view/View\$OnClickListener;)V",
                false
            )

            // 跳过原始的方法调用
            return
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf)
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
        mv.visitFieldInsn(
            Opcodes.GETSTATIC,
            "cn/losefeather/library_tracker/entity/EventContact",
            "EVENT_ON_CLICK",
            "Ljava/lang/String;"
        )

        // 创建HashMap并添加View ID
        mv.visitTypeInsn(Opcodes.NEW, "java/util/HashMap")
        mv.visitInsn(Opcodes.DUP)
        mv.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "java/util/HashMap",
            "<init>",
            "()V",
            false
        )
        mv.visitInsn(Opcodes.DUP)
        mv.visitLdcInsn("view_id")
        mv.visitVarInsn(Opcodes.ILOAD, 2)
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "java/lang/Integer",
            "valueOf",
            "(I)Ljava/lang/Integer;",
            false
        )
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/util/HashMap",
            "put",
            "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            false
        )
        mv.visitInsn(Opcodes.POP)

        // 添加View类名
        mv.visitInsn(Opcodes.DUP)
        mv.visitLdcInsn("view_class")
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/Object",
            "getClass",
            "()Ljava/lang/Class;",
            false
        )
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/Class",
            "getName",
            "()Ljava/lang/String;",
            false
        )
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/util/HashMap",
            "put",
            "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            false
        )
        mv.visitInsn(Opcodes.POP)

        // 添加View资源名
        mv.visitInsn(Opcodes.DUP)
        mv.visitLdcInsn("view_resource")
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "android/view/View",
            "getResources",
            "()Landroid/content/res/Resources;",
            false
        )
        mv.visitVarInsn(Opcodes.ILOAD, 2)
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "android/content/res/Resources",
            "getResourceEntryName",
            "(I)Ljava/lang/String;",
            false
        )
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/util/HashMap",
            "put",
            "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            false
        )
        mv.visitInsn(Opcodes.POP)

        // 调用trackUiEvent方法
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "cn/losefeather/library_tracker/EventTracker",
            "trackUiEvent",
            "(Ljava/lang/String;Ljava/util/HashMap;)V",
            false
        )
    }
}