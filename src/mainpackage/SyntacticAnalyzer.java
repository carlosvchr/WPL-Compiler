package mainpackage;

import java.io.IOException;
import java.util.ArrayList;

public class SyntacticAnalyzer {

	// Analizador léxico al que iremos requiriendo símbolos
	LexicalAnalyzer lex = null;
	
	// Array que contiene los no terminales con la expresión que generan
	ArrayList<String[]> nt;
	
	/** @param Fichero fuente */
	public SyntacticAnalyzer(String path) {
		lex = new LexicalAnalyzer();
		lex.start(path);
	}
	
	/** Inicia el procesado del fuente */
	public void start() {
		
	}
	
	
}
