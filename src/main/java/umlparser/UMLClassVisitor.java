package umlparser;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class UMLClassVisitor extends ClassVisitor {
    private ClassNode classInfo;

    public UMLClassVisitor(int api, ClassNode classInfo) {
        super(api);
        this.classInfo = classInfo;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        String className = name.replace('/', '.');
        classInfo.setClassName(className);

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        String fieldType = Type.getType(descriptor).getClassName();
        FieldNode f = new FieldNode(name, fieldType, access);
        classInfo.addField(f);
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        String returnType = Type.getReturnType(descriptor).getClassName();

        Type[] argTypes = Type.getArgumentTypes(descriptor);
        List<String> paramTypeNames = new ArrayList<>();
        for (Type t : argTypes) {
            paramTypeNames.add(t.getClassName());
        }

        MethodNode m = new MethodNode(name, returnType, paramTypeNames, access);
        classInfo.addMethod(m);

        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}