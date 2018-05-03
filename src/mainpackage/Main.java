package mainpackage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {

	public static void main(String[] args) {
		//String path = "/home/carlos/Escritorio/pruebas.txt";
		String path = "/home/carlos/Escritorio/pruebaswpl.txt";
		String output = "/home/carlos/Escritorio/pruebasOutput.html";
		
		SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(path, output);
		syntacticAnalyzer.start();
		
//		testLexical(path);
		
		System.out.println("Compilation successfully.");
	}

	
	/** Imprime los símbolos generados por el analizador léxico */
	public static void testLexical(String path) {
		LexicalAnalyzer la = new LexicalAnalyzer();
		la.start(path);
		
		Symbol s;
		while((s = la.next()).sym() != Lexer.__end) {	
				System.out.println(s.sym()+putSpaces(20-s.sym().length())+(s.val()!=null ? s.val():""));
		}
	}
	
	/** devuelve una cadena de texto compuesta por 'n' espacios */
	public static String putSpaces(int n) {
		String ret = "";
		for(int i=0; i<n; i++) {
			ret += " ";
		}
		return ret;
	}
	
	/** Permite redireccionar la salida de consola a un fichero */
	public static void standardIO2File(String fileName){
        if(fileName.equals("")){//Si viene vacío usamos este por defecto
            fileName="/home/carlos/Escritorio/log.txt";
        }

        try {
            //Creamos un printstream sobre el archivo permitiendo añadir al
            //final para no sobreescribir.
            PrintStream ps = new PrintStream(new BufferedOutputStream(
                    new FileOutputStream(new File(fileName),true)),true);
            //Redirigimos entrada y salida estandar
            System.setOut(ps);
            System.setErr(ps);
        } catch (FileNotFoundException ex) {
            System.err.println("Se ha producido una excepción FileNotFoundException");
        }
    }
}
