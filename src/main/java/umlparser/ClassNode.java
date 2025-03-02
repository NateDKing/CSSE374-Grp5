package umlparser;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassNode extends ClassVisitor {
    private String className;
    private String superName;
    private String classType;
    private boolean isDecorator = false;
    private List<FieldNode> fields = new ArrayList<>();
    private List<MethodNode> methods = new ArrayList<>();
    private List<String> interfaces = new ArrayList<>();

    
    public ClassNode(int api) {
        super(api);
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        String className = name.replace('/', '.');
        setClassName(className);
        setInterfaces(interfaces);
        String newSuperName = superName.toString().replace('/', '.');
        setSuperName(newSuperName);
        setClassType(access);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
    	String fieldType;
    	if (signature != null) {
        	fieldType = Type.getType(signature).getClassName();
        	fieldType = fieldType.replace("<L", "<").replace(";>", ">");
        	String[] parts = fieldType.split("<");
        	parts[0] = parts[0].split("\\.")[parts[0].split("\\.").length - 1];
        	fieldType = parts[0] + "<" + parts[1];
    	} else {
    		fieldType = Type.getType(descriptor).getClassName();
    	}
        FieldNode f = new FieldNode(name, fieldType, access);
        addField(f);
        return super.visitField(access, name, descriptor, signature, value);
    }

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
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
    
    public void setInterfaces(String[] interfaces) {
    	for (String interfac : interfaces)
		if (interfac.substring(0,9).equals("umlparser")) {
			this.interfaces.add(interfac.substring(10));
		}
    }
    
    public List<String> getInterfaces() {
        return interfaces;
    }
    
    public void setSuperName(String superName) {
    	if (superName.substring(0,9).equals("umlparser")) {
    		this.superName = superName;
    	}
    }
    
    public String getSuperName() {
        return superName;
    }
    
    public void setClassType(int opcode) {
    	if ((opcode & Opcodes.ACC_INTERFACE) != 0) {
    		this.classType = "interface";
    	} else if ((opcode & Opcodes.ACC_ABSTRACT) != 0) {
    		this.classType = "abstract";
    	} else {
    		this.classType = "class";
    	}
    }

    public String getClassType() {
        return classType;
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
    
    public boolean getSingleton() {
    	// Singleton (Dogs)
		// - private static Dog dog
    	boolean isSelfInstance = false;
    	for (FieldNode fieldNode : getFields()) {
    		boolean isAccess = fieldNode.getAccess().equals("private");
    		boolean isStatic = fieldNode.getIsStatic();
    		boolean isName   = fieldNode.getType().equals(className);
    		if (isAccess && isStatic && isName) {
    			isSelfInstance = true;
    			break;
    		}
    	}
    	
		// - private Logger() {}
    	boolean isConstructor = false;
    	for (MethodNode methodNode : getMethods()) {
    		boolean isAccess = methodNode.getAccess().equals("private");
    		boolean isName = methodNode.getName().equals("<init>");
    		if (isAccess && isName) {
    			isConstructor = true;
    			break;
    		}
    	}
    	
		// - A Method: public static w/ return type of self
    	boolean isPubStat = false;
    	for (MethodNode methodNode : getMethods()) {
    		if (methodNode.getName().equals("<init>")) {
    			continue;
    		}
    		boolean isAccess = methodNode.getAccess().equals("public");
    		boolean isStatic = methodNode.getIsStatic();
    		boolean returnSelf = methodNode.getReturnType().equals(className);
    		if (isAccess && isStatic && returnSelf) {
    			isPubStat = true;
    			break;
    		}
    	}
    	
		return isSelfInstance && isConstructor && isPubStat;
	}
    
    public boolean singletonAbuse() {
    	if (fields.size() == 1) {
    		return true;
    	} else {
    		return false;
    	}
    }

    public boolean getDecorator() {
    	for(MethodNode methodNode : getMethods()) {
    		if(methodNode.getName().equals("<init>")) {
    			for(String param : methodNode.getParameterTypes()) {
    				if(param.equals(superName)) {
    					setDecorator(true);
    					return true;
    				}
    				
    				for(String intf : interfaces) {
    					if(param.length() > 10 && param.substring(10).equals(intf)) {
    						setDecorator(true);
    						return true;
    					}
    				}
    			}
    		}
    	}
    	// setDecorator(false);
    	return false;
    } 
    
    public boolean isDecorator() {
    	return this.isDecorator;
    }
    
    public void setDecorator(Boolean b) {
    	if(!isDecorator) {
    		this.isDecorator = b;
    	}
    }
}