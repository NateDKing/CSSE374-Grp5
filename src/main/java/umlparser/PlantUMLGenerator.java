package umlparser;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Opcodes;

public class PlantUMLGenerator {
    
	private ArrayList<ClassNode> classNodes;
	
	public PlantUMLGenerator() {
		classNodes = new ArrayList<ClassNode>();
    }
	
	public void addClassNode(ClassNode classInfo) {
		classNodes.add(classInfo);
	}
	
	public StringBuilder generateClassUML(ClassNode classInfo) {
		StringBuilder sb = new StringBuilder();

        sb.append("class ").append(classInfo.getClassName()).append(" {\n");
        
        for (FieldNode field : classInfo.getFields()) {
            String visibilitySymbol = getVisibilitySymbol(field.getAccess());
            sb.append("  ")
              .append(visibilitySymbol)
              .append(" ")
              .append(field.getName())
              .append(" : ")
              .append(field.getType())
              .append("\n");
        }
        
        for (MethodNode method : classInfo.getMethods()) {
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
        
        return sb;
		
	}
	
    public String generateUML() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("@startuml\n");
        
        for (ClassNode classNode : classNodes) {
        	sb.append(generateClassUML(classNode));
        }
        
        // Lines
        // - Extends
        
        sb.append("@enduml\n");
        
        return sb.toString();
    }
    
    private String getVisibilitySymbol(int access) {
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