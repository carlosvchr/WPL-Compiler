package mainpackage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class WebPL {

	private static PrintStream ps;
	
	public WebPL() {
		
	}
	
	public CompResult compile(String input, String output) {
		CompResult compilationOutput = new CompResult();
		SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(input, output, compilationOutput);
		boolean compilation = syntacticAnalyzer.start();
		
		if(compilation) {
			compilationOutput.add("Compilation successfully.");
			compilationOutput.setResult(true);
		}else {
			compilationOutput.add("Unresolved compilation problems.");
			compilationOutput.setResult(false);
		}
		return compilationOutput;
	}
	
	/** Redirect the prints to a file */
	public static void standardIO2File(String fileName){
        try {
            //Creamos un printstream sobre el archivo permitiendo añadir al
            //final para no sobreescribir.
            ps = new PrintStream(new BufferedOutputStream(
                    new FileOutputStream(new File(fileName),true)),true);
            //Redirigimos entrada y salida estandar
            System.setOut(ps);
            System.setErr(ps);
        } catch (FileNotFoundException ex) {
            System.err.println("Error FileNotFoundException");
        }
    }
	
}
