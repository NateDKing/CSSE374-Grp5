package umlparser;

import java.util.ArrayList;
import java.util.List;

public class ClassNode {
    private String className;
    private List<FieldNode> fields = new ArrayList<>();
    private List<MethodNode> methods = new ArrayList<>();

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
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