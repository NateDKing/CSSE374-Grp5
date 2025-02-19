package umlparser;

import java.util.List;

import org.objectweb.asm.Opcodes;

public class MethodNode {
    private String name;
    private String returnType;
    private List<String> parameterTypes;
    private String access;
    private boolean isStatic = false;

    public MethodNode(String name, String returnType, List<String> parameterTypes, int access) {
        this.name = name;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        
    	if ((access & Opcodes.ACC_PRIVATE) != 0) {
    		this.access = "private";
    	} else if ((access & Opcodes.ACC_PUBLIC) != 0) {
    		this.access = "public";
    	} else if ((access & Opcodes.ACC_PROTECTED) != 0) {
    		this.access = "protected";
    	} else {
    		this.access = "packageProtected";
    	}
    	
    	if ((access & Opcodes.ACC_STATIC) != 0) {
    		this.isStatic = true;
    	}
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<String> getParameterTypes() {
        return parameterTypes;
    }

    public String getAccess() {
        return access;
    }
    
    public boolean getIsStatic() {
        return isStatic;
    }
}