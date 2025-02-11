package umlparser;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class Main {
    public static void main(String[] args) {
        try {
            ClassInfo classInfo = new ClassInfo();
            ClassReader cr = new ClassReader(new FileInputStream("build/classes/java/test/umlparser/ExampleClass.class"));
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
}