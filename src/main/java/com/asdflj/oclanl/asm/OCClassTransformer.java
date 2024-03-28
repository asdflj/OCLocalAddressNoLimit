package com.asdflj.oclanl.asm;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class OCClassTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] code) {
        if (transformedName.equals("li.cil.oc.server.component.InternetCard$")) {
            Transform tform = InternetCardTransformer.INSTANCE;
            return tform.transformClass(code);
        } else {
            return code;
        }
        // return code;
    }

    public interface Transform {

        byte[] transformClass(byte[] code);
    }

    public abstract static class ClassMapper implements Transform {

        @Override
        public byte[] transformClass(byte[] code) {
            ClassReader reader = new ClassReader(code);
            ClassWriter writer = new ClassWriter(reader, getWriteFlags());
            reader.accept(getClassMapper(writer), ClassReader.EXPAND_FRAMES);
            return writer.toByteArray();
        }

        protected int getWriteFlags() {
            return 0;
        }

        protected abstract ClassVisitor getClassMapper(ClassVisitor downstream);
    }
}
