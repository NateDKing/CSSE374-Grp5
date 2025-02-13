package umlparser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class Main {
    public static void main(String[] args) {
        try {
            List<ClassReader> classes = getClassesInPackage();
//            ClassReader cr = new ClassReader(new FileInputStream("build/classes/java/test/umlparser/ExampleClass.class"));
            for (ClassReader cr : classes) {
            	ClassInfo classInfo = new ClassInfo();
            	UMLClassVisitor cv = new UMLClassVisitor(Opcodes.ASM9, classInfo);
            	cr.accept(cv, 0);
            	String uml = UMLDiagramGenerator.generateUML(classInfo);
            	
            	try (FileOutputStream fos = new FileOutputStream("diagram.plantuml")) {
                    fos.write(uml.getBytes());
                }

                System.out.println("Writing UML diagram...");
                System.setProperty("GRAPHVIZ_DOT", "/opt/homebrew/bin/dot");
                generateSVG(uml, "diagram.svg");
                System.out.println("Wrote UML diagram to diagram.svg");
            }
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateSVG(String plantUmlSource, String outputFilePath) throws IOException {
	    SourceStringReader reader = new SourceStringReader(plantUmlSource);
	    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
	        reader.outputImage(os, new FileFormatOption(FileFormat.SVG));
	        os.close();
	
	        String svg = os.toString("UTF-8");
	        if (svg.startsWith("<?xml")) {
	            svg = svg.substring(svg.indexOf("?>") + 2).trim();
	        }
	        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
	            fos.write(svg.getBytes("UTF-8"));
	        }
	    }
    }
    
    /*
     * Edited from https://stackoverflow.com/questions/28678026/how-can-i-get-all-class-files-in-a-specific-package-in-java
     */
    public static final List<ClassReader> getClassesInPackage() {
    	String packageName = "build/classes/java/test/umlparser";
    	packageName = packageName.replaceAll("/", File.separator);
        
    	File folder = new File(packageName);
    	String[] classPathEntries = folder.list();
    	
    	
    	List<ClassReader> classes = new ArrayList<>();
//        String[] classPathEntries = System.getProperty("java.class.path").split(
//                System.getProperty("path.separator")
//        );

//        String name;
        for (String classpathEntry : classPathEntries) {
            try {
                String base = new String(packageName + File.separatorChar + classpathEntry);
                if (base.endsWith(".class")) {
                	classes.add(new ClassReader(new FileInputStream(base)));
                }
//                for (File file : base.listFiles()) {
//                    name = file.getName();
//                    if (name.endsWith(".class")) {
//                    	new ClassReader(new FileInputStream(base
//                        name = name.substring(0, name.length() - 6);
//                        classes.add(Class.forName(packageName + "." + name));
//                    }
//                }
            } catch (Exception ex) {
                // Silence is gold
            }
        }

        return classes;
    }
}

