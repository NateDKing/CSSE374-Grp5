package umlparser;

import java.util.List;

public class MethodNode {
    private String name;
    private String returnType;
    private List<String> parameterTypes;
    private int access;

    public MethodNode(String name, String returnType, List<String> parameterTypes, int access) {
        this.name = name;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.access = access;
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

    public int getAccess() {
        return access;
    }
}