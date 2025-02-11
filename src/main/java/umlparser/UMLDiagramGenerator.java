package umlparser;

import java.util.List;

import org.objectweb.asm.Opcodes;

public class UMLDiagramGenerator {
    
    public static String generateUML(ClassInfo classInfo) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("@startuml\n");
        
        sb.append("class ").append(classInfo.getClassName()).append(" {\n");
        
        for (FieldInfo field : classInfo.getFields()) {
            String visibilitySymbol = getVisibilitySymbol(field.getAccess());
            sb.append("  ")
              .append(visibilitySymbol)
              .append(" ")
              .append(field.getName())
              .append(" : ")
              .append(field.getType())
              .append("\n");
        }
        
        for (MethodInfo method : classInfo.getMethods()) {
            String visibilitySymbol = getVisibilitySymbol(method.getAccess());
            sb.append("  ")
              .append(visibilitySymbol)
              .append(" ")
              .append(method.getName())
              .append("(");

            List<String> params = method.getParameterTypes();
            for (int i = 0; i < params.size(); i++) {
                sb.append(params.get(i));
                if (i < params.size() - 1) {
                    sb.append(", ");
                }
            }
            
            sb.append(") : ")
              .append(method.getReturnType())
              .append("\n");
        }
        
        sb.append("}\n");
        sb.append("@enduml\n");
        
        return sb.toString();
    }
    
    private static String getVisibilitySymbol(int access) {
        if ((access & Opcodes.ACC_PUBLIC) != 0) {
            return "+";
        } else if ((access & Opcodes.ACC_PROTECTED) != 0) {
            return "#";
        } else if ((access & Opcodes.ACC_PRIVATE) != 0) {
            return "-";
        }
        return "~";
    }
}