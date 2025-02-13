package umlparser;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class ClassNode extends ClassVisitor {
    private String className;
    private List<FieldNode> fields = new ArrayList<>();
    private List<MethodNode> methods = new ArrayList<>();
    private String[] interfaces;
    private String superName;
    
    public ClassNode(int api) {
        super(api);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        String className = name.replace('/', '.');
        setClassName(className);
        setInterfaces(interfaces);
        String newSuperName = superName.toString().replace('/', '.');
        setSuperName(newSuperName);

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        String fieldType = Type.getType(descriptor).getClassName();
        FieldNode f = new FieldNode(name, fieldType, access);
        addField(f);
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
        addMethod(m);

        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    public void setClassName(String className) {
        this.className = className.substring(10);;
    }

    public String getClassName() {
        return className;
    }
    
    public void setInterfaces(String[] interfaces) {
        this.interfaces = interfaces;
    }
    
    public String[] getInterfaces() {
        return interfaces;
    }
    
    public void setSuperName(String superName) {
    	if (superName.substring(0,9).equals("umlparser")) {
    		this.superName = superName.substring(10);
    	}
    }

    public String getSuperName() {
        return superName;
    }

    public void addField(FieldNode field) {
        fields.add(field);
    }

    public List<FieldNode> getFields() {
        return fields;
    }

    public void addMethod(MethodNode method) {
        methods.add(method);
    }

    public List<MethodNode> getMethods() {
        return methods;
    }
}