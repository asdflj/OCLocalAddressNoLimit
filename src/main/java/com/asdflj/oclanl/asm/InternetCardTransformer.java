package com.asdflj.oclanl.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class InternetCardTransformer extends OCClassTransformer.ClassMapper {

    public static final InternetCardTransformer INSTANCE = new InternetCardTransformer();

    @Override
    protected ClassVisitor getClassMapper(ClassVisitor downstream) {
        return new TransformInternetCard(Opcodes.ASM5, downstream);
    }

    private static class TransformInternetCard extends ClassVisitor {

        TransformInternetCard(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if ("isRequestAllowed".equals(name)) {
                return new TransformIsRequestAllowed(api, super.visitMethod(access, name, desc, signature, exceptions));
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }
    }

    private static class TransformIsRequestAllowed extends MethodVisitor {

        TransformIsRequestAllowed(int api, MethodVisitor mv) {
            super(api, mv);
        }

        private boolean allow = true;

        @Override
        public void visitInsn(int opcode) {
            if (opcode == Opcodes.ICONST_0) {
                allow = false;
            } else if (opcode == Opcodes.IRETURN && !allow) {
                super.visitVarInsn(Opcodes.ALOAD, 1);
                super.visitVarInsn(Opcodes.ALOAD, 2);
                super.visitVarInsn(Opcodes.ALOAD, 3);
                super.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    "com/asdflj/oclanl/asm/Hooker",
                    "isRequestAllowed",
                    "(Lli/cil/oc/Settings;Ljava/net/InetAddress;Ljava/lang/String;)Z",
                    false);
                allow = true;
            }
            super.visitInsn(opcode);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }

}
