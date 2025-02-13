package umlparser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class JCUT {
    public static void main(String[] args) {
        try {
            List<ClassReader> classes = getClassesInPackage();
            
            //Empty outputFiles folder
            Files.walk(Path.of("outputFiles"))
            .filter(path -> !path.equals(Path.of("outputFiles")))
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
            
            PlantUMLGenerator plantUMLGenerator = new PlantUMLGenerator();
            
            for (ClassReader cr : classes) {
            	ClassNode classNode = new ClassNode(Opcodes.ASM9);
            	cr.accept(classNode, 0);
            	plantUMLGenerator.addClassNode(classNode);
            	
            }
            	
            String uml = plantUMLGenerator.generateUML();
            	
        	String outputPath = "outputFiles/plantUML";
        	outputPath = outputPath.replaceAll("/", File.separator);
        	
        	try (FileOutputStream fos = new FileOutputStream(outputPath + ".plantuml")) {
                fos.write(uml.getBytes());
            }

            System.out.println("Writing UML diagram...");
            System.setProperty("GRAPHVIZ_DOT", "/opt/homebrew/bin/dot");
            generateSVG(uml, outputPath + ".svg");
            System.out.println("Wrote UML diagram to " + outputPath + ".svg");
        
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
    	String packageName = "bin/test/umlparser";
    	packageName = packageName.replaceAll("/", File.separator);
        
    	File folder = new File(packageName);
    	String[] classPathEntries = folder.list();
    	
    	
    	List<ClassReader> classes = new ArrayList<>();
        for (String classpathEntry : classPathEntries) {
            try {
                String base = new String(packageName + File.separatorChar + classpathEntry);
                if (base.endsWith(".class")) {
                	classes.add(new ClassReader(new FileInputStream(base)));
                }
            } catch (Exception ex) {
                // Silence is gold
            }
        }

        return classes;
    }
}

