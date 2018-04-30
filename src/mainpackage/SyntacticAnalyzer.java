package mainpackage;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class SyntacticAnalyzer {
	
	/** En esta clase se procede con la siguiente implementación: *********************************************
	 * 
	 * <PROGRAM>			-> <IMPORTS> <META> <DEFINES> <TAGS>
	 * <IMPORTS>			-> import dp <VALS> pc <IMPORTS> | $
	 * <META>				-> meta dp <VALS> pc <META> | $
	 * <DEFINES>			-> define dp <VALS> pc <DEFINES> | $
	 *
	 * <TAGS>				-> <TAG> <TAGS> | $
	 * <TAG>				-> <CONTAINER> | <TABLE> | <COMPONENT> | <INCLUDE>
	 *
	 * <CONTAINER>			-> container dp op <ATTRS> <TAGS> cp
	 * <TABLE>				-> table dp op <ATTRS> <HROW> <ROWS> cp
	 * <HROW>				-> hrow dp op <TAGS> cp | $
	 * <ROWS>				-> row dp op <TAGS> cp <ROWS> | $
	 * <COMPONENT>			-> component dp text? (op <ATTRS> cp)?
	 * <INCLUDE>			-> include dp <VALS> pc
	 *
	 * <ATTRS>				-> attr dp <VALS> pc <ATTRS> | $
	 *
	 * <VALS>				-> <VAL> | <VAL> <VALS>
	 * <VAL>				-> val | color | text | var | int | id | measure | bool
	 *
	 *********************************************************************************************************/
	
	
	/**
	 * 
	 * Para poder procesar el mayor numero de errores sintacticos, una vez se haya detectado
	 * uno, mostraremos el error, y buscaremos un punto seguro para continuar analizando errores.
	 * 
	 * Este punto seguro será el siguiente al No Terminal que haya producido el error. 
	 * 
	 * */
	
	/** No terminales */
	private enum NT { META, IMPORTS, DEFINES, TAG, CONTAINER, TABLE, HROW,
					ROWS, COMPONENT, INCLUDE, ATTRS, VALS }

	// Analizador léxico al que iremos requiriendo símbolos
	LexicalAnalyzer lex = null;
//	
//	// Analizador semantico que comprobara que los valores asociados a los atributos son correctos
	SemanticAnalyzer sem = null;
//	
//	// CodeGenerator es la clase que genera el código
//	CodeGenerator generator = null;
//	
//	// Pila donde registramos las etiquetas abiertas para cerrarlas cuando llegue un cp
//	ArrayDeque<Symbol> openedTagsStac;
//	
//	
	/** @param Fichero fuente 
	 *  @param Fichero donde se genera el código */
	public SyntacticAnalyzer(String path, String output) {
//		generator = new CodeGenerator(output);
		sem = new SemanticAnalyzer();
		lex = new LexicalAnalyzer();
		lex.start(path);
	}
	
	/** Inicia el procesado del fuente */
	public void start() {
		analyzeProgram();
	}
	
	/** Muestra un mensaje de error sintáctico */
	private void printSyntacticError(String s) {
		Symbol saux = lex.next();
		System.err.println("Syntactic Analyzer: Error found on line "+lex.getLineNumber()+".: ..."+saux.val()+" ("+saux.sym()+"); Expected: "+s);
	}
	
//	/** Intenta llegar a un punto seguro tras un error para retomar el análisis */
//	private boolean recover(String nextTag) {
//		// Analizar siguientes del NT que haya producido el error
//	
//		return false;
//	}
	
	/** Comprueba si un terminal forma parte de los primeros de un no terminal */
	private boolean first(NT nt, String t) {
		switch (nt){
		case META:
			return (t.compareTo(Lexer.__meta) == 0);
		case IMPORTS:
			return (t.compareTo(Lexer._import) == 0);
		case DEFINES:
			return (t.compareTo(Lexer._define) == 0);
		case TABLE:
			return (t.compareTo(Lexer._table) == 0);
		case HROW:
			return (t.compareTo(Lexer._hrow) == 0);
		case ROWS:
			return (t.compareTo(Lexer._row) == 0);
		case TAG:
			return (t.compareTo(Lexer.__container) == 0 || t.compareTo(Lexer.__component) == 0 || 
					t.compareTo(Lexer._include) == 0) || t.compareTo(Lexer._table) == 0;
		case CONTAINER:
			return (t.compareTo(Lexer.__container) == 0);
		case COMPONENT:
			return (t.compareTo(Lexer.__component) == 0);
		case INCLUDE:
			return (t.compareTo(Lexer._include) == 0);
		case ATTRS:
			return (t.compareTo(Lexer.__attr) == 0);
		case VALS:
			return (t.compareTo(Lexer.__val) == 0 || t.compareTo(Lexer.__var) == 0 || 
					t.compareTo(Lexer.__text) == 0 || t.compareTo(Lexer.__color) == 0 || 
					t.compareTo(Lexer.__integer) == 0 || t.compareTo(Lexer.__measure) == 0 || 
					t.compareTo(Lexer.__bool) == 0 || t.compareTo(Lexer.__identifier) == 0 );
		default: return false;
		}
	}
		
	/** Comprueba si un terminal forma parte de los siguientes de un no terminal 
	 * TODO: Esta funcion esta sin hacer, los datos corresponden a los de la funcion first()*/
	private boolean following(NT nt, String t) {
		
		return false;
	}
	
	/** <PROGRAM> -> <IMPORTS> <META> <DEFINES> <TAG> */
	private boolean analyzeProgram() {
		
		// Genera las etiquetas de apertura html
//		generator.startHead();
			
		analyze(NT.IMPORTS);
		analyze(NT.META);
		analyze(NT.DEFINES);
		
		// Genera la etiqueta de cierre del head y apertura del body
//		generator.startBody();
		
		analyze(NT.TAG);
		
		if(lex.nextAndUndo().sym() != Lexer.__end) {
			printSyntacticError("$ program");
			return false;
		}
		
		// Genera las etiquetas de cierre html
//		generator.end();
		
		return true;
	}
	
	
//	/** <IMPORTS> -> import <VALS> pc <IMPORTS> | $ */
//	private void analyzeImports() {	
//		Symbol s = lex.nextAndUndo();
//		
//		if(s.sym().compareTo(Lexer._import) != 0) {
//			printSyntacticError("import import");
//			return;
//		}
//		
//		s = lex.nextAndUndo();
//		if(!first(NT.VALS, s.sym())) {
//			printSyntacticError("val import");
//			return;
//		}else {
//			Symbol[] r = analyzeVals();
//			// Si no hay errores semanticos generamos el codigo
////			if(sem.validate(importSym, r)) {
////				generator.genImport(r[0].val());
////			}
//		}
//		
//		if(lex.next().sym().compareTo(Lexer.__pc) != 0) {
//			printSyntacticError("PC import");
//			return;
//		}
//		
//		s = lex.nextAndUndo();
//		if(first(NT.VALS, s.sym())) {
//			analyzeImports();
//		}
//
//	}
	
	
	/** <IMPORTS> -> import dp <VALS> pc <IMPORTS> | $ */
	private void analyzeImports() {	
		analyze(Lexer._import, false);
		analyze(Lexer.__dp, false);
		analyze(NT.VALS);
		analyze(Lexer.__pc, false);
		analyze(NT.IMPORTS);
	}
	
	/** <META> -> meta dp <VALS> pc <META> | $*/
	private void analyzeMeta() {
		analyze(Lexer.__meta, false);
		analyze(Lexer.__dp, false);
		analyze(NT.VALS);
		analyze(Lexer.__pc, false);
		analyze(NT.META);
	}
		
	/** <DEFINES> -> define dp <VALS> pc <DEFINES> | $ */
	private void analyzeDefines() {
		if(!analyze(Lexer._define, false)) return;
		if(!analyze(Lexer.__dp, false)) return;
		if(!analyze(NT.VALS)) return;
		if(!analyze(Lexer.__pc, false)) return;
		analyze(NT.DEFINES);
	}
	
	/** <TAGS> -> <TAG> <TAGS> | $
	 * <TAG> -> <CONTAINER> | <TABLE> | <COMPONENT> | <INCLUDE> */
	private void analyzeTags() {
		if(analyze(NT.CONTAINER));
		else if(analyze(NT.COMPONENT));
		else if(analyze(NT.TABLE));
		else if(analyze(NT.DEFINES));
		else printSyntacticError("TAGS");
		analyze(NT.TAG);
	}
	
	/** <CONTAINER> -> container dp op <ATTRS> <TAGS> cp */
	private void analyzeContainer() {
		analyze(Lexer.__container, false);
		analyze(Lexer.__dp, false);
		analyze(Lexer.__op, false);
		analyze(NT.ATTRS);
		analyze(NT.TAG);
		analyze(Lexer.__cp, false);
	}
	
	/** <TABLE> -> table dp op <ATTRS> <HROW> <ROWS> cp */
	private void analyzeTable() {
		analyze(Lexer._table, false);
		analyze(Lexer.__dp, false);
		analyze(Lexer.__op, false);
		analyze(NT.ATTRS);
		analyze(NT.HROW);
		analyze(NT.ROWS);
		analyze(Lexer.__cp, false);
	}
	
	/** <HROW> -> hrow dp op <TAGS> cp | $ */
	private void analyzeHrow() {
		analyze(Lexer._hrow, false);
		analyze(Lexer.__dp, false);
		analyze(Lexer.__op, false);
		analyze(NT.TAG);
		analyze(Lexer.__cp, false);
	}
	
	/** <ROWS> -> row dp op <TAGS> cp <ROWS> | $ */
	private void analyzeRows() {
		analyze(Lexer._row, false);
		analyze(Lexer.__dp, false);
		analyze(Lexer.__op, false);
		analyze(NT.TAG);
		analyze(Lexer.__cp, false);
		analyze(NT.ROWS);
	}
	
	/** <COMPONENT> -> component dp text? (op <ATTRS> cp)? */
	private void analyzeComponent() {
		analyze(Lexer.__component, false);
		analyze(Lexer.__dp, false);
		analyze(Lexer.__text, true);
		if(analyze(Lexer.__op, true)) {
			analyze(NT.ATTRS);
			analyze(Lexer.__cp, false);
		}else {
			lex.undo();
			analyze(Lexer.__pc, false);
		}
	}
	
	/** <INCLUDE> -> include dp <VALS> pc */
	private void analyzeIncludes() {
		analyze(Lexer._include, false);
		analyze(Lexer.__dp, false);
		analyze(NT.VALS);
		analyze(Lexer.__pc, false);
	}
	
	/** <ATTRS> -> attr dp <VALS> pc <ATTRS> | $ */
	private void analyzeAttrs() {
		analyze(Lexer.__attr, false);
		analyze(Lexer.__dp, false);
		analyze(NT.VALS);
		analyze(Lexer.__pc, false);
		analyze(NT.ATTRS);
	}
	
	/** <VALS> -> <VAL> | <VAL> <VALS>
	 * <VAL> -> val | color | text | var | int | id | measure | bool */
	private void analyzeVals() {
		if(analyze(Lexer.__val, false));
		else if(analyze(Lexer.__color, false));
		else if(analyze(Lexer.__text, false));
		else if(analyze(Lexer.__var, false));
		else if(analyze(Lexer.__integer, false));
		else if(analyze(Lexer.__identifier, false));
		else if(analyze(Lexer.__measure, false));
		else if(analyze(Lexer.__bool, false));
		else printSyntacticError("VAL");
		analyze(NT.VALS);
	}
	
	
	/** Analiza un terminal, el terminal puede ser o no anulable */
	private boolean analyze(String terminal, boolean nullable) {
		String next = lex.next().sym();
		switch(next) {
		case Lexer.__val:
		case Lexer.__var:
		case Lexer.__bool:
		case Lexer.__color:
		case Lexer.__text:
		case Lexer.__measure:
		case Lexer.__integer:
		case Lexer.__identifier:
			next = Lexer.__val; break;
		
		}
		if(next.compareTo(terminal) != 0) {
			if(!nullable)
				printSyntacticError(terminal);
			return false;
		}	
		return true;
	}
	
	/** Analiza un no terminal */
	private boolean analyze(NT nonterminal) {
		boolean match = first(nonterminal, lex.nextAndUndo().sym());
		switch(nonterminal) {
		case META:
			if(match) {	analyzeMeta(); return true; } else { return false; }
		case IMPORTS:
			if(match) {	analyzeImports(); return true; } else { return false; }
		case DEFINES:
			if(match) { analyzeDefines(); return true; } else { return false; }
		case TAG:
			if(match) {	analyzeTags(); return true; } else { return false; }
		case CONTAINER:
			if(match) {	analyzeContainer(); return true; } else { return false; }
		case COMPONENT:
			if(match) {	analyzeComponent(); return true; } else { return false; }
		case INCLUDE:
			if(match) {	analyzeIncludes(); return true; } else { return false; }
		case ATTRS:
			if(match) {	analyzeAttrs(); return true; } else { return false; }
		case VALS:
			if(match) {	analyzeVals(); return true; } else { return false; }
		case TABLE:
			if(match) {	analyzeTable(); return true; } else { return false; }
		case HROW:
			if(match) {	analyzeHrow(); return true; } else { return false; }
		case ROWS:
			if(match) {	analyzeRows(); return true; } else { return false; }
		default: System.out.println("ERROR"); return false;
		}
	}
	
	
	public void print(String s) {
		System.out.println(s);
	}
	
}
