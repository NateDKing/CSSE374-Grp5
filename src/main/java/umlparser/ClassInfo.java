package umlparser;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {
    private String className;
    private List<FieldInfo> fields = new ArrayList<>();
    private List<MethodInfo> methods = new ArrayList<>();

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void addField(FieldInfo field) {
        fields.add(field);
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public void addMethod(MethodInfo method) {
        methods.add(method);
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }
}