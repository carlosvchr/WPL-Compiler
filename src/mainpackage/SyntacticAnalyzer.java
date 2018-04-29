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
	 * <TABLE>				-> table dp op <HROW> <ROWS> cp
	 * <HROW>				-> hrow dp op <TAGS> cp | $
	 * <ROWS>				-> row dp op <TAGS> cp <ROWS> | $
	 * <COMPONENT>			-> component <VAL> dp op <ATTRS> cp
	 * <INCLUDE>			-> include <VALS> pc
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
					ROWS, COMPONENT, INCLUDE, ATTRS, VALS, VAL, VALNULLABLE }

	// Analizador léxico al que iremos requiriendo símbolos
	LexicalAnalyzer lex = null;
//	
//	// Analizador semantico que comprobara que los valores asociados a los atributos son correctos
//	SemanticAnalyzer sem = null;
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
//		sem = new SemanticAnalyzer();
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
		case TAG:
			return (t.compareTo(Lexer.__container) == 0 || t.compareTo(Lexer._table) == 0 || 
					t.compareTo(Lexer.__component) == 0 || t.compareTo(Lexer._include) == 0);
		case CONTAINER:
			return (t.compareTo(Lexer.__container) == 0);
		case COMPONENT:
			return (t.compareTo(Lexer.__component) == 0);
		case INCLUDE:
			return (t.compareTo(Lexer._include) == 0);
		case ATTRS:
			return (t.compareTo(Lexer.__attr) == 0);
		case VALNULLABLE:
		case VAL:
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
		switch (nt){
		case META:
			return (t.compareTo(Lexer.__meta) == 0);
		case IMPORTS:
			return (t.compareTo(Lexer._import) == 0);
		case DEFINES:
			return (t.compareTo(Lexer._define) == 0);
		case TAG:
			return (t.compareTo(Lexer.__container) == 0 || t.compareTo(Lexer._table) == 0 || 
					t.compareTo(Lexer.__component) == 0 || t.compareTo(Lexer._include) == 0);
		case CONTAINER:
			return (t.compareTo(Lexer.__container) == 0);
		case COMPONENT:
			return (t.compareTo(Lexer.__component) == 0);
		case INCLUDE:
			return (t.compareTo(Lexer._include) == 0);
		case ATTRS:
			return (t.compareTo(Lexer.__attr) == 0);
		case VAL:
			return (t.compareTo(Lexer.__val) == 0 || t.compareTo(Lexer.__var) == 0 || 
					t.compareTo(Lexer.__text) == 0 || t.compareTo(Lexer.__color) == 0 || 
					t.compareTo(Lexer.__integer) == 0 || t.compareTo(Lexer.__measure) == 0 || 
					t.compareTo(Lexer.__bool) == 0 || t.compareTo(Lexer.__identifier) == 0 );
		default: return false;
		}
	}
	
	/** <PROGRAM> -> <IMPORTS> <META> <DEFINES> <TAG> */
	private boolean analyzeProgram() {
		
		// Genera las etiquetas de apertura html
//		generator.startHead();
		
		String sym = lex.nextAndUndo().sym();
		if(first(NT.IMPORTS, sym))
			analyzeImports();	
		
		sym = lex.nextAndUndo().sym();
		if(first(NT.META, sym))
			analyzeMeta();
		
		sym = lex.nextAndUndo().sym();
		if(first(NT.DEFINES, sym))
			analyzeDefines();
		
		// Genera la etiqueta de cierre del head y apertura del body
//		generator.startBody();
		
		sym = lex.nextAndUndo().sym();
		if(first(NT.TAG, sym))
			analyzeTags();
		
		if(lex.nextAndUndo() != null) {
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
		analyze(Lexer._import);
		analyze(Lexer.__dp);
		analyze(NT.VALS);
		analyze(Lexer.__pc);
		analyze(NT.IMPORTS);
	}
	
	/** <META> -> meta dp <VALS> pc <META> | $*/
	private void analyzeMeta() {
		analyze(Lexer.__meta);
		analyze(Lexer.__dp);
		analyze(NT.VALS);
		analyze(Lexer.__pc);
		analyze(NT.META);
	}
		
	/** <DEFINES> -> define dp <VALS> pc <DEFINES> | $ */
	private void analyzeDefines() {
		if(!analyze(Lexer._define)) return;
		if(!analyze(Lexer.__dp)) return;
		if(!analyze(NT.VALS)) return;
		if(!analyze(Lexer.__pc)) return;
		analyze(NT.DEFINES);
	}
	
	/** <TAGS> -> <TAG> <TAGS> | $
	 * <TAG> -> <CONTAINER> | <TABLE> | <COMPONENT> | <INCLUDE> */
	private void analyzeTags() {
		analyze(NT.CONTAINER);
		analyze(NT.COMPONENT);
		analyze(NT.TABLE);
		analyze(NT.DEFINES);
		analyze(NT.TAG);
	}
	
	/** <CONTAINER> -> container dp op <ATTRS> <TAGS> cp */
	private void analyzeContainer() {
		analyze(Lexer.__container);
		analyze(Lexer.__dp);
		analyze(Lexer.__op);
		analyze(NT.ATTRS);
		analyze(NT.TAG);
		analyze(Lexer.__cp);
	}
	
	/** <TABLE> -> table dp op <HROW> <ROWS> cp */
	private void analyzeTable() {
		analyze(Lexer._table);
		analyze(Lexer.__dp);
		analyze(Lexer.__op);
		analyze(NT.HROW);
		analyze(NT.ROWS);
		analyze(Lexer.__cp);
	}
	
	/** <HROW> -> hrow dp op <TAGS> cp | $ */
	private void analyzeHrow() {
		analyze(Lexer._hrow);
		analyze(Lexer.__dp);
		analyze(Lexer.__op);
		analyze(NT.TAG);
		analyze(Lexer.__cp);
	}
	
	/** <ROWS> -> row dp op <TAGS> cp <ROWS> | $ */
	private void analyzeRows() {
		analyze(Lexer._row);
		analyze(Lexer.__dp);
		analyze(Lexer.__op);
		analyze(NT.TAG);
		analyze(Lexer.__cp);
		analyze(NT.ROWS);
	}
	
	/** <COMPONENT> -> component <VAL> dp op <ATTRS> cp */
	private void analyzeComponent() {
		analyze(Lexer.__component);
		analyze(NT.VALNULLABLE);
		analyze(Lexer.__dp);
		analyze(Lexer.__op);
		analyze(NT.ATTRS);
		analyze(Lexer.__cp);
	}
	
	/** <INCLUDE> -> include <VALS> pc */
	private void analyzeIncludes() {
		analyze(Lexer._include);
		analyze(NT.VALS);
		analyze(Lexer.__pc);
	}
	
	/** <ATTRS> -> attr dp <VALS> pc <ATTRS> | $ */
	private void analyzeAttrs() {
		analyze(Lexer.__attr);
		analyze(Lexer.__dp);
		analyze(NT.VALS);
		analyze(Lexer.__pc);
		analyze(NT.ATTRS);
	}
	
	/** <VALS> -> <VAL> | <VAL> <VALS>
	 * <VAL>				-> val | color | text | var | int | id | measure | bool */
	private void analyzeVals() {
		analyze(Lexer.__val);
		analyze(Lexer.__color);
		analyze(Lexer.__text);
		analyze(Lexer.__var);
		analyze(Lexer.__integer);
		analyze(Lexer.__identifier);
		analyze(Lexer.__measure);
		analyze(Lexer.__bool);
		analyze(NT.VALS);
	}
	
	
	/** Analiza un terminal */
	private boolean analyze(String terminal) {
		if(lex.next().sym().compareTo(terminal) != 0) {
			printSyntacticError(terminal);
			return false;
		}	
		return true;
	}
	
	/** Analiza un no terminal */
	private boolean analyze(NT nonterminal) {
		boolean match = first(NT.VALS, lex.nextAndUndo().sym());
		switch(nonterminal) {
		case VALS:
			if(match) {
				analyzeVals();
				return true;
			}else {
				printSyntacticError("VALS");
				return false;
			}
		case META:
			if(match) {	analyzeMeta(); return true;	}else {	return false; }
		case DEFINES:
			if(match) { analyzeDefines(); return true; }else { return false; }
		default: return first(NT.VALS, lex.nextAndUndo().sym());
		}
	}
	
}
