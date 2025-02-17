package umlparser;

import java.util.ArrayList;
import java.util.Arrays;
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

		boolean isSingleton = classInfo.getSingleton();
		boolean isDecorator = classInfo.getDecorator();

		if (isSingleton) {
			String labelName = classInfo.getClassName() + "Label";
			sb.append("label \" \" as " + labelName + "\n");
			sb.append(labelName + " -[#blue]-> " + cleanName(classInfo.getClassName()) + " : \"Singleton\"\n");
		}

		if (isDecorator) {
			String labelName = classInfo.getClassName() + "Label";
			sb.append("label \" \" as " + labelName + "\n");
			sb.append(labelName + " -[#red]-> " + cleanName(classInfo.getClassName()) + " : \"Decorator\"\n");
		}

		sb.append(classInfo.getClassType() + " ").append(cleanName(classInfo.getClassName()));

		if (isSingleton) {
			if (!classInfo.singletonAbuse()) {
				System.out.println("Singleton Abuse");
				sb.append(" <<Singleton Abuse>>");
			} else {
				System.out.println("Singleton");
				sb.append(" <<Singleton>>");
			}
		}

		if (isDecorator) {
			sb.append(" <<Decorator>>");
		}

		sb.append(" {\n");

		for (FieldNode field : classInfo.getFields()) {
			String visibilitySymbol = getVisibilitySymbol(field.getAccess());
			sb.append("  ").append(visibilitySymbol).append(" ").append(cleanName(field.getName())).append(" : ")
					.append(cleanName(field.getType())).append("\n");
		}

		for (MethodNode method : classInfo.getMethods()) {
			String visibilitySymbol = getVisibilitySymbol(method.getAccess());
			sb.append("  ").append(visibilitySymbol).append(" ").append(cleanName(method.getName())).append("(");

			List<String> params = method.getParameterTypes();
			for (int i = 0; i < params.size(); i++) {
				sb.append(params.get(i));
				if (i < params.size() - 1) {
					sb.append(", ");
				}
			}

			sb.append(") : ").append(method.getReturnType()).append("\n");
		}

		sb.append("}\n\n");

		return sb;

	}

	public StringBuilder generateDependenciesUML(ClassNode classInfo) {
		StringBuilder sb = new StringBuilder();

		// - Extends (superName)
		// Pawn --|> Piece
		if (classInfo.getSuperName() != null) {
			sb.append(cleanName(classInfo.getClassName())).append(" --|>").append(cleanName(classInfo.getSuperName())).append("\n");
		}

		// - Implements (interfaces)
		// - Pawn ..|> xxx
		for (String interfac : classInfo.getInterfaces()) {
			sb.append(cleanName(classInfo.getClassName())).append(" ..|>").append(interfac).append("\n");
		}

		List<String> addedClasses = new ArrayList<>();

		for (FieldNode field : classInfo.getFields()) {
			// add []
			if (field.getType().length() < 10) {
				continue;
			}

//			if (!field.getType().substring(0, 9).equals("umlparser")) {
//				continue;
//			}

			String toClassName = field.getType().substring(10).replaceAll("[^a-zA-Z]", "");

//			if (addedClasses.contains(toClassName)) {
//				
//			}
			if (!field.getType().contains("umlparser.")) {
				continue;
			}
			
			String fieldClass = field.getType();
			
			if (fieldClass.contains("<") && fieldClass.contains(">")) {
				fieldClass = fieldClass.substring(fieldClass.indexOf('<') + 1, fieldClass.indexOf('>'));
			}
			
			fieldClass = fieldClass.replace("umlparser.", "");
//			fieldClass = fieldClass.replaceAll("[^a-zA-Z]", "");
			
			if (addedClasses.contains(fieldClass)) {
				continue;
			}

			String[] keyWords = { "[", "<", "List" };
			String quantity = "1";
			if (Arrays.stream(keyWords).anyMatch(field.getType()::contains)) {
				quantity = "*";
			}
			// Board --> "*" Square
			// String cleaned = input.replaceAll("[^a-zA-Z]", "");
			sb.append(cleanName(classInfo.getClassName())).append(" --> \"").append(quantity).append("\" ").append(cleanName(fieldClass))
					.append("\n");
			addedClasses.add(fieldClass);
		}

		// Singleton (Dogs)
		// - private static Dog dog
		// - private Dog() {}
		// - All Methods: public static

		// Decerator

		return sb;

	}

	public String generateUML() {
		StringBuilder sb = new StringBuilder();

		sb.append("@startuml\n");
		sb.append("allowmixing\n");
		sb.append("skinparam class {\n");
		sb.append("BackgroundColor<<Decorator>> Salmon\n");
		sb.append("BackgroundColor<<Singleton>> LightBlue\n");
		sb.append("BackgroundColor<<Singleton Abuse>> LightBlue\n");
		sb.append("BorderColor<<Singleton Abuse>> Orange\n");
		sb.append("BorderThickness<<Singleton Abuse>> 3\n");
		sb.append("}\n");

		for (ClassNode classNode : classNodes) {
			sb.append(generateClassUML(classNode));
		}

		for (ClassNode classNode : classNodes) {
			sb.append(generateDependenciesUML(classNode));
		}

		sb.append("@enduml\n");

		return sb.toString();
	}

	private String cleanName(String name) {
		return name.split("\\.")[name.split("\\.").length - 1];
	}

	private String getVisibilitySymbol(String access) {
		if (access.equals("public")) {
			return "+";
		} else if (access.equals("protected")) {
			return "#";
		} else if (access.equals("private")) {
			return "-";
		}
		return "~";
	}
}