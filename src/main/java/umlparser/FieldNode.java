package umlparser;

import org.objectweb.asm.Opcodes;

public class FieldNode {
    private String name;
    private String type;
    private String access;
    private boolean isStatic = false;

    public FieldNode(String name, String type, int access) {
        this.name = name;
        this.type = type;
        
        if ((access & Opcodes.ACC_PRIVATE) != 0) {
    		this.access = "private";
    	} else if ((access & Opcodes.ACC_PUBLIC) != 0) {
    		this.access = "public";
    	} else if ((access & Opcodes.ACC_PROTECTED) != 0) {
    		this.access = "protected";
    	} else {
    		this.access = "packagePrivate";
    	}
        
        if ((access & Opcodes.ACC_STATIC) != 0) {
    		this.isStatic = true;
    	}
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAccess() {
        return access;
    }
    
    public boolean getIsStatic() {
        return isStatic;
    }
}