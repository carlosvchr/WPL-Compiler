package mainpackage;

import java.util.Stack;

public class SyntacticAnalyzer {
	
	private enum NT {
	    META, METANR, IMPORTS, IMPORT, DEFINES, DEFINE, TAG, TAGNR, CONTAINER,
	    ITEMCONTAINER, RADIOGROUP, COMPONENT, INCLUDE, ITEMS, RADIOBUTTONS, 
	    RADIOBUTTON, ATTRS, ATTR, VALS, VAL
	}

	// Analizador léxico al que iremos requiriendo símbolos
	LexicalAnalyzer lex = null;
	
	// Pila donde registramos las etiquetas abiertas para cerrarlas cuando llegue un cp
	Stack<String> openedTagsStack;
	
	/** @param Fichero fuente */
	public SyntacticAnalyzer(String path) {
		lex = new LexicalAnalyzer();
		lex.start(path);
	}
	
	/** Inicia el procesado del fuente */
	public void start() {
		analyzeProgram();
	}
	
	/** Muestra un mensaje de error */
	private void printError() {
		System.err.println("Syntactic Analyzer: Error found on line "+lex.getLineNumber()+".: ..."+lex.next().val());
	}
	
	/** Comprueba si un terminal forma parte de uno de un no terminal */
	private boolean belongs(NT nt, String t) {
		switch (nt){
		case META:
			return (t.compareTo(Lexer._meta) == 0);
		case METANR:
			return (t.compareTo(Lexer._meta) == 0);
		case IMPORTS:
			return (t.compareTo(Lexer._import) == 0);
		case IMPORT:
			return (t.compareTo(Lexer._import) == 0);
		case DEFINES:
			return (t.compareTo(Lexer._define) == 0);
		case DEFINE:
			return (t.compareTo(Lexer._define) == 0);
		case TAG:
			return (t.compareTo(Lexer._container) == 0 || t.compareTo(Lexer._itemcont) == 0 ||
					t.compareTo(Lexer._radiogroup) == 0 || t.compareTo(Lexer._component) == 0 ||
					t.compareTo(Lexer._include) == 0);
		case TAGNR:
			return (t.compareTo(Lexer._container) == 0 || t.compareTo(Lexer._itemcont) == 0 ||
			t.compareTo(Lexer._radiogroup) == 0 || t.compareTo(Lexer._component) == 0 ||
			t.compareTo(Lexer._include) == 0);
		case CONTAINER:
			return (t.compareTo(Lexer._container) == 0);
		case ITEMCONTAINER:
			return (t.compareTo(Lexer._itemcont) == 0);
		case RADIOGROUP:
			return (t.compareTo(Lexer._radiogroup) == 0);
		case COMPONENT:
			return (t.compareTo(Lexer._component) == 0);
		case INCLUDE:
			return (t.compareTo(Lexer._include) == 0);
		case ITEMS:
			return (t.compareTo(Lexer._item) == 0);
		case RADIOBUTTONS:
			return (t.compareTo(Lexer._radiobutton) == 0);
		case RADIOBUTTON:
			return (t.compareTo(Lexer._radiobutton) == 0);
		case ATTRS:
			return (t.compareTo(Lexer._attr) == 0);
		case ATTR:
			return (t.compareTo(Lexer._attr) == 0);
		case VALS:
			return (t.compareTo(Lexer._bool) == 0 || t.compareTo(Lexer._color) == 0 || 
					t.compareTo(Lexer._font) == 0 || t.compareTo(Lexer._tdecor) == 0 || 
					t.compareTo(Lexer._align) == 0 || t.compareTo(Lexer._effect) == 0 || 
					t.compareTo(Lexer._animation) == 0 || t.compareTo(Lexer._charset) == 0 || 
					t.compareTo(Lexer._integer) == 0 || t.compareTo(Lexer._real) == 0 || 
					t.compareTo(Lexer._text) == 0 || t.compareTo(Lexer._definetype) == 0 || 
					t.compareTo(Lexer._none) == 0 );
		case VAL:
			return (t.compareTo(Lexer._bool) == 0 || t.compareTo(Lexer._color) == 0 || 
					t.compareTo(Lexer._font) == 0 || t.compareTo(Lexer._tdecor) == 0 || 
					t.compareTo(Lexer._align) == 0 || t.compareTo(Lexer._effect) == 0 || 
					t.compareTo(Lexer._animation) == 0 || t.compareTo(Lexer._charset) == 0 || 
					t.compareTo(Lexer._integer) == 0 || t.compareTo(Lexer._real) == 0 || 
					t.compareTo(Lexer._text) == 0 || t.compareTo(Lexer._definetype) == 0 || 
					t.compareTo(Lexer._none) == 0 );
		default: return false;
		}
	}
	
	/** <PROGRAM> */
	private boolean analyzeProgram() {
		String sym = lex.next().sym();
		lex.undo();
		
		if(belongs(NT.META, sym)) 		analyzeMeta();	
		if(belongs(NT.IMPORTS, sym))	analyzeImports();
		if(belongs(NT.DEFINES, sym))	analyzeDefines();
		if(belongs(NT.TAG, sym)) 		analyzeTag();
		
		if(lex.next() != null) {
			lex.undo();
			printError();
			return false;
		}
		
		return true;
	}
	
	/** <META> */
	private boolean analyzeMeta() {
		// <METANR>
		analyzeMetaNr();
		
		if(belongs(NT.METANR, lex.next().sym())) {
			lex.undo();
			analyzeMeta();
		}
		// Retornamos (en esta etiqueta no importa el valor devuelto porque es anulable)
		return true;
	}
	
	
	/** <METANR> */
	private boolean analyzeMetaNr() {		

		
		return true;
	}
	
	
	/** <IMPORTS> */
	private boolean analyzeImports() {
		
		return true;
	}
	
	
	/** <IMPORT> */
	private boolean analyzeImport() {
		
		return true;
	}
	
	
	/** <DEFINES> */
	private boolean analyzeDefines() {
		
		
		return true;
	}
	
	
	/** <DEFINE> */
	private boolean analyzeDefine() {
		
		
		return true;
	}
	
	
	/** <TAG> */
	private boolean analyzeTag() {
		
		return true;
	}
	
	
	/** <TAGNR> */
	private boolean analyzeTagnr() {
		
		return true;
	}
	
	
	/** <CONTAINER> */
	private boolean analyzeContainer() {
		return false;
	}
	
	
	/** <ITEMCONTAINER> */
	private boolean analyzeItemcontainer() {
		return false;
	}
	
	
	/** <RADIOGROUP> */
	private boolean AnalyzeRadiogroup() {
		return false;
	}
	
	
	/** <COMPONENT> */
	private boolean analyzeComponent() {
		return false;
	}
	
	
	/** <INCLUDE> */
	private boolean analyzeInclude() {
		return false;
	}
	
	
	/** <ITEMS> */
	private boolean analyzeItems() {
		return false;
	}
	
	
	/** <RADIOBUTTONS> */
	private boolean analyzeRadiobuttons() {
		return false;
	}
	
	
	/** <RADIOBUTTON> */
	private boolean analyzeRadiobutton() {
		return false;
	}
	
	
	/** <ATTRS> */
	private boolean analyzeAttrs() {
		return false;
	}
	
	
	/** <ATTR> */
	private boolean analyzeAttr() {
		return false;
	}
	
	
	/** <VALS> */
	private boolean analyzeVals() {
		return false;
	}
	
	
	/** <VAL> */
	private boolean analyzeVal() {
		return false;
	}
	
}
