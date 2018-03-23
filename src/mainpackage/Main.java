package mainpackage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {

	public static void main(String[] args) {
		//standardIO2File("/home/carlos/Escritorio/outputwpl.txt");
		
		//String path = "/home/carlos/Escritorio/pruebawpl.txt";
		String path = "/home/carlos/Escritorio/pruebas.txt";
		
		SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(path);
		syntacticAnalyzer.start();
		
		LexicalAnalyzer la = new LexicalAnalyzer();
		la.start(path);
		
		String sym;
		while((sym = la.getNext()) != null) {
			if(sym.length() > 0)
				System.out.println(sym);
		}
		
		//System.out.println(REManager.validate("^\\t", "	a"));
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
