package cn.losefeather.plugin_tracker

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class ViewOnClickMethodVisitor(mv: MethodVisitor, access: Int, name: String?, descriptor: String?) :
    AdviceAdapter(Opcodes.ASM9, mv, access, name, descriptor) {

    private var hasAutoTrackAnnotation = false
    private var pageName: String? = null



//    override fun onMethodExit(opcode: Int) {
//        // 插入埋点代码
//        insertTrackCode()
//
//        super.onMethodExit(opcode)
//    }

    private fun isComposePage(descriptor: String) {

    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        println("visitAnnotation $descriptor || $visible")
        descriptor?.apply {
            if (this.contains("Lcn/losefeather/library_tracker/annotation/AutoTrack;")) {
                hasAutoTrackAnnotation = true
                return object : AnnotationVisitor(api, super.visitAnnotation(descriptor, visible)) {
                    override fun visit(name: String?, value: Any?) {
                        if (name == "pageName" && value is String) {
                            pageName = value
                        }
                        super.visit(name, value)
                    }
                }
            }
        }
        return super.visitAnnotation(descriptor, visible)
    }

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

}