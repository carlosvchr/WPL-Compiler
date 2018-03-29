package mainpackage;

import java.util.Stack;

public class SyntacticAnalyzer {
	
	/**
	 * 
	 * Para poder procesar el mayor numero de errores sintacticos, una vez se haya detectado
	 * uno, mostraremos el error, pero simularemos que emitimos un valor aceptable para que
	 * pueda continuar el procesado. 
	 * 
	 * */
	
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
	private void printSyntacticError(String s) {
		lex.undo();
		Symbol saux = lex.next();
		System.err.println("Syntactic Analyzer: Error found on line "+lex.getLineNumber()+".: ..."+saux.val()+" ("+saux.sym()+"); Expected: "+s);
	}
	
	/** Comprueba si un terminal forma parte de los primeros de un no terminal */
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
					t.compareTo(Lexer._none) == 0 || t.compareTo(Lexer._measure) == 0);
		case VAL:
			return (t.compareTo(Lexer._bool) == 0 || t.compareTo(Lexer._color) == 0 || 
					t.compareTo(Lexer._font) == 0 || t.compareTo(Lexer._tdecor) == 0 || 
					t.compareTo(Lexer._align) == 0 || t.compareTo(Lexer._effect) == 0 || 
					t.compareTo(Lexer._animation) == 0 || t.compareTo(Lexer._charset) == 0 || 
					t.compareTo(Lexer._integer) == 0 || t.compareTo(Lexer._real) == 0 || 
					t.compareTo(Lexer._text) == 0 || t.compareTo(Lexer._definetype) == 0 || 
					t.compareTo(Lexer._none) == 0 || t.compareTo(Lexer._measure) == 0);
		default: return false;
		}
	}
	
	/** <PROGRAM> -> <IMPORTS> <META> <DEFINES> <TAG> */
	private boolean analyzeProgram() {
		String sym = lex.next().sym();
		lex.undo();
		if(belongs(NT.IMPORTS, sym))
			analyzeImports();	
		
		sym = lex.next().sym();
		lex.undo();
		if(belongs(NT.META, sym))
			analyzeMeta();
		
		sym = lex.next().sym();
		lex.undo();
		if(belongs(NT.DEFINES, sym))
			analyzeDefines();
		
		sym = lex.next().sym();
		lex.undo();
		if(belongs(NT.TAG, sym))
			analyzeTag();
		
		if(lex.next() != null) {
			printSyntacticError("$ program");
			return false;
		}
		
		return true;
	}
	
	/** <META> -> <METANR> <META> | $ */
	private boolean analyzeMeta() {
		// <METANR>
		analyzeMetaNr();
		
		Symbol s = lex.next();
		lex.undo();
		if(s != null) {
			if(belongs(NT.META, s.sym())) {
				analyzeMeta();
			}
		}
		// Retornamos (en esta etiqueta no importa el valor devuelto porque es anulable)
		return true;
	}
	
	
	/** <METANR> -> meta dp <VALS> pc */
	private boolean analyzeMetaNr() {		
		if(lex.next().sym().compareTo(Lexer._meta) != 0) {
			printSyntacticError("meta metanr");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._dp) != 0) {
			printSyntacticError("DP metanr");
			return false;
		}
		
		Symbol s = lex.next();
		if(!belongs(NT.VALS, s.sym())) {
			printSyntacticError("val metanr");
			return false;
		}else {
			lex.undo();
			analyzeVals();
		}
		s = lex.next();
		lex.undo();
		
		if(lex.next().sym().compareTo(Lexer._pc) != 0) {
			printSyntacticError("PC metanr");
			return false;
		}

		return true;
	}
	
	
	/** <IMPORTS> -> <IMPORT> <IMPORTS> | $ */
	private boolean analyzeImports() {
		// <IMPORT>
		analyzeImport();
		
		Symbol s = lex.next();
		lex.undo();
		if(s != null) {
			if(belongs(NT.IMPORTS, s.sym())) {
				analyzeImports();
			}
		}
		// Retornamos (en esta etiqueta no importa el valor devuelto porque es anulable)
		return true;
	}
	
	
	/** <IMPORT> -> import <VALS> pc */
	private boolean analyzeImport() {	
		if(lex.next().sym().compareTo(Lexer._import) != 0) {
			printSyntacticError("import import");
			return false;
		}
		
		Symbol s = lex.next();
		if(!belongs(NT.VALS, s.sym())) {
			printSyntacticError("val import");
			return false;
		}else {
			lex.undo();
			analyzeVals();
		}
		
		if(lex.next().sym().compareTo(Lexer._pc) != 0) {
			printSyntacticError("PC import");
			return false;
		}
		
		return true;
	}
	
	
	/** <DEFINES> -> <DEFINE> <DEFINES> | $ */
	private boolean analyzeDefines() {
		// <DEFINE>
		analyzeDefine();
		
		Symbol s = lex.next();
		lex.undo();
		if(s != null) {
			if(belongs(NT.DEFINES, s.sym())) {
				lex.undo();
				analyzeDefines();
			}
		}
		// Retornamos (en esta etiqueta no importa el valor devuelto porque es anulable)
		return true;
	}
	
	
	/** <DEFINE> -> define dp op type dp <VALS> pc name dp <VALS> pc content dp <VALS> pc cp */
	private boolean analyzeDefine() {		
		if(lex.next().sym().compareTo(Lexer._define) != 0) {
			printSyntacticError("define");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._dp) != 0) {
			printSyntacticError("DP define");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._op) != 0) {
			printSyntacticError("OP define");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._type) != 0) {
			printSyntacticError("type define");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._dp) != 0) {
			printSyntacticError("DP define");
			return false;
		}
		
		Symbol s = lex.next();
		if(!belongs(NT.VALS, s.sym())) {
			printSyntacticError("val define");
			return false;
		}else {
			lex.undo();
			analyzeVals();
		}
		
		if(lex.next().sym().compareTo(Lexer._pc) != 0) {
			printSyntacticError("PC define");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._name) != 0) {
			printSyntacticError("name define");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._dp) != 0) {
			printSyntacticError("DP define");
			return false;
		}
		
		s = lex.next();
		if(!belongs(NT.VALS, s.sym())) {
			printSyntacticError("val define");
			return false;
		}else {
			lex.undo();
			analyzeVals();
		}
		
		if(lex.next().sym().compareTo(Lexer._pc) != 0) {
			printSyntacticError("PC define");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._content) != 0) {
			printSyntacticError("content define");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._dp) != 0) {
			printSyntacticError("DP define");
			return false;
		}
		
		s = lex.next();
		if(!belongs(NT.VALS, s.sym())) {
			printSyntacticError("val define");
			return false;
		}else {
			lex.undo();
			analyzeVals();
		}
		
		if(lex.next().sym().compareTo(Lexer._pc) != 0) {
			printSyntacticError("PC define");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._cp) != 0) {
			printSyntacticError("CP define");
			return false;
		}
		
		return true;
	}
	
	
	/** <TAG> -> <TAGNR> <TAG> | $ */
	private boolean analyzeTag() {
		// <TAGNR>
		analyzeTagnr();
		
		Symbol s = lex.next();
		lex.undo();
		if(s != null) {
			if(belongs(NT.TAGNR, s.sym())) {
				analyzeTag();
			}
		}
		// Retornamos (en esta etiqueta no importa el valor devuelto porque es anulable)
		return true;
	}
	
	
	/** <TAGNR>	-> <CONTAINER> | <ITEMCONTAINER> | <RADIOGROUP> | <COMPONENT> | <INCLUDE> */
	private boolean analyzeTagnr() {
		
		Symbol s = lex.next(); 
		lex.undo();
		if(belongs(NT.CONTAINER, s.sym())) {
			analyzeContainer();
			return true;
		}
		
		s = lex.next();
		lex.undo();
		if(belongs(NT.ITEMCONTAINER, s.sym())) {
			analyzeItemcontainer();
			return true;
		}
		 
		s = lex.next();
		lex.undo();
		if(belongs(NT.RADIOGROUP, s.sym())) {
			AnalyzeRadiogroup();
			return true;
		}
	
		s = lex.next();
		lex.undo();
		if(belongs(NT.COMPONENT, s.sym())) {
			analyzeComponent();
			return true;
		}
		 
		s = lex.next();
		lex.undo();
		if(belongs(NT.INCLUDE, s.sym())) {
			analyzeInclude();
			return true;
		}
		 
		return false;
	}
	
	
	/** <CONTAINER>	-> container dp op <ATTRS> <TAG> cp */
	private boolean analyzeContainer() {
		
		if(lex.next().sym().compareTo(Lexer._container) != 0) {
			printSyntacticError("container container");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._dp) != 0) {
			printSyntacticError("DP container");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._op) != 0) {
			printSyntacticError("OP container");
			return false;
		}
		
		Symbol s = lex.next();
		lex.undo();
		if(belongs(NT.ATTRS, s.sym())) {
			analyzeAttrs();
		}
		
		s = lex.next();
		lex.undo();
		if(belongs(NT.TAG, s.sym())) {
			analyzeTag();
		}
		
		if(lex.next().sym().compareTo(Lexer._cp) != 0) {
			printSyntacticError("CP container");
			return false;
		}
		
		return true;
	}
	
	
	/** <ITEMCONTAINER>	-> itemcont dp op <ATTRS> <ITEMS> cp */
	private boolean analyzeItemcontainer() {
		
		if(lex.next().sym().compareTo(Lexer._itemcont) != 0) {
			printSyntacticError("itemcont itcont");
			return false;
		}

		if(lex.next().sym().compareTo(Lexer._dp) != 0) {
			printSyntacticError("DP itcont");
			return false;
		}

		if(lex.next().sym().compareTo(Lexer._op) != 0) {
			printSyntacticError("OP itcont");
			return false;
		}

		Symbol s = lex.next();
		lex.undo();
		if(belongs(NT.ATTRS, s.sym())) {
			analyzeAttrs();
		}

		s = lex.next();
		lex.undo();
		if(belongs(NT.ITEMS, s.sym())) {
			analyzeItems();
		}
		
		if(lex.next().sym().compareTo(Lexer._cp) != 0) {
			printSyntacticError("CP itcont");
			return false;
		}
		
		return true;
	}
	
	
	/** <RADIOGROUP> -> radiogroup dp op <ATTRS> <RADIOBUTTONS> cp */
	private boolean AnalyzeRadiogroup() {

		if(lex.next().sym().compareTo(Lexer._radiogroup) != 0) {
			printSyntacticError("radiogroup radiogroup");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._dp) != 0) {
			printSyntacticError("DP radiogroup");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._op) != 0) {
			printSyntacticError("OP radiogroup");
			return false;
		}
		
		Symbol s = lex.next();
		lex.undo();
		if(belongs(NT.ATTRS, s.sym())) {
			analyzeAttrs();
		}
		
		s = lex.next();
		lex.undo();
		if(belongs(NT.RADIOBUTTONS, s.sym())) {
			analyzeRadiobuttons();
		}
		
		if(lex.next().sym().compareTo(Lexer._cp) != 0) {
			printSyntacticError("CP radiogroup");
			return false;
		}
		
		return true;
	}
	
	
	/** <COMPONENT>	-> component dp op <ATTRS> cp */
	private boolean analyzeComponent() {
		
		if(lex.next().sym().compareTo(Lexer._component) != 0) {
			printSyntacticError("component component");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._dp) != 0) {
			printSyntacticError("DP component");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._op) != 0) {
			printSyntacticError("OP component");
			return false;
		}
		
		Symbol s = lex.next();
		lex.undo();
		if(belongs(NT.ATTRS, s.sym())) {
			analyzeAttrs();
		}

		if(lex.next().sym().compareTo(Lexer._cp) != 0) {
			printSyntacticError("CP component");
			return false;
		}
		
		return true;
	}
	
	
	/** <INCLUDE> -> include <VALS> pc */
	private boolean analyzeInclude() {
		
		if(lex.next().sym().compareTo(Lexer._include) != 0) {
			printSyntacticError("include include");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._op) != 0) {
			printSyntacticError("OP include");
			return false;
		}
		
		Symbol s = lex.next();
		if(belongs(NT.VALS, s.sym())) {
			lex.undo();
			analyzeVals();
		}else {
			printSyntacticError("val include");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._pc) != 0) {
			printSyntacticError("PC include");
			return false;
		}
		
		return true;
	}
	
	/** <ITEMS> -> <ITEM> <ITEMS> | $ */
	private boolean analyzeItems() {
		// <ITEM>
		analyzeItem();
		
		Symbol s = lex.next();
		lex.undo();
		if(belongs(NT.ITEMS, s.sym())) {
			analyzeItems();
		}
		// Retornamos (en esta etiqueta no importa el valor devuelto porque es anulable)
		return true;
	}
	
	/** <ITEM> -> item dp op header dp <VALS> pc <TAG> cp */
	private boolean analyzeItem() {

		if(lex.next().sym().compareTo(Lexer._item) != 0) {
			printSyntacticError("item item");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._dp) != 0) {
			printSyntacticError("DP item");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._op) != 0) {
			printSyntacticError("OP item");
			return false;
		}
				
		if(lex.next().sym().compareTo(Lexer._header) != 0) {
			printSyntacticError("header item");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._dp) != 0) {
			printSyntacticError("DP item");
			return false;
		}
		
		Symbol s = lex.next();
		if(belongs(NT.VALS, s.sym())) {
			lex.undo();
			analyzeVals();
		}else {
			printSyntacticError("val item");
		}
		
		if(lex.next().sym().compareTo(Lexer._pc) != 0) {
			printSyntacticError("PC item");
			return false;
		}

		s = lex.next();
		lex.undo();
		if(belongs(NT.TAG, s.sym())) {
			analyzeTag();
		}

		if(lex.next().sym().compareTo(Lexer._cp) != 0) {
			printSyntacticError("CP item");
			return false;
		}
				
		return true;
	}
	
	
	/** <RADIOBUTTONS> */
	private boolean analyzeRadiobuttons() {
		// <RADIOBUTTON>
		analyzeRadiobutton();
		
		Symbol s = lex.next();
		lex.undo();
		if(belongs(NT.RADIOBUTTONS, s.sym())) {
			analyzeRadiobuttons();
		}
		// Retornamos (en esta etiqueta no importa el valor devuelto porque es anulable)
		return true;
	}
	
	
	/** <RADIOBUTTON> -> radiobutton dp op <ATTRS> cp */
	private boolean analyzeRadiobutton() {
		
		if(lex.next().sym().compareTo(Lexer._radiobutton) != 0) {
			printSyntacticError("radiobutton radiobutton");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._dp) != 0) {
			printSyntacticError("DP radiobutton");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._op) != 0) {
			printSyntacticError("OP radiobutton");
			return false;
		}
		
		Symbol s = lex.next();
		lex.undo();
		if(belongs(NT.ATTRS, s.sym())) {
			analyzeAttrs();
		}
		
		if(lex.next().sym().compareTo(Lexer._cp) != 0) {
			printSyntacticError("CP radiobutton");
			return false;
		}
		
		return true;
	}
	
	
	/** <ATTRS>	-> <ATTR> <ATTRS> | $ */
	private boolean analyzeAttrs() {
		// <ATTR>
		analyzeAttr();

		Symbol s = lex.next();
		lex.undo();
		if(belongs(NT.ATTRS, s.sym())) {
			analyzeAttrs();
		}

		// Retornamos (en esta etiqueta no importa el valor devuelto porque es anulable)
		return true;
	}
	
	
	/** <ATTR> -> attr dp <VALS> pc */
	private boolean analyzeAttr() {
		
		if(lex.next().sym().compareTo(Lexer._attr) != 0) {
			printSyntacticError("attr attr");
			return false;
		}
		
		if(lex.next().sym().compareTo(Lexer._dp) != 0) {
			printSyntacticError("DP attr");
			return false;
		}
		
		Symbol s = lex.next();
		if(belongs(NT.VALS, s.sym())) {
			lex.undo();
			analyzeVals();
		}else {
			printSyntacticError("val attr");
		}

		if(lex.next().sym().compareTo(Lexer._pc) != 0) {
			printSyntacticError("PC attr");
			return false;
		}
		return true;
	}
	
	
	/** <VALS> -> <VAL> | <VAL> coma <VALS> */
	private boolean analyzeVals() {
		
		analyzeVal();
		
		while(lex.next().sym().compareTo(Lexer._coma) == 0) {
			if(!belongs(NT.VAL, lex.next().sym())) {
				printSyntacticError("val vals");
			}
		}
		lex.undo();
		
		return true;
	}
	
	
	/** <VAL> */
	private boolean analyzeVal() {
		
		String s = lex.next().sym();
		if(s.compareTo(Lexer._bool) != 0 && s.compareTo(Lexer._color) != 0 &&
				s.compareTo(Lexer._font) != 0 && s.compareTo(Lexer._tdecor) != 0 &&
				s.compareTo(Lexer._align) != 0 && s.compareTo(Lexer._effect) != 0 &&
				s.compareTo(Lexer._animation) != 0 && s.compareTo(Lexer._charset) != 0 &&
				s.compareTo(Lexer._integer) != 0 && s.compareTo(Lexer._real) != 0 &&
				s.compareTo(Lexer._text) != 0 && s.compareTo(Lexer._definetype) != 0 &&
				s.compareTo(Lexer._none) != 0 && s.compareTo(Lexer._measure) != 0) {
			printSyntacticError("val val");
			return false;
		}
		
		return true;
	}
	
	public void print(String s) {
		System.out.println(s);
	}
	
	public void print(Symbol s) {
		System.out.println(s.sym() + " .. " + s.val());
	}
	
}
