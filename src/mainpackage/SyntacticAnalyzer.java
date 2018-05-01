package mainpackage;


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
	 * <HROW>				-> hrow dp op <ATTRS> <TAGS> cp | $
	 * <ROWS>				-> row dp op <ATTRS> <TAGS> cp <ROWS> | $
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
	CodeGenerator gen = null;
//	
//	// Pila donde registramos las etiquetas abiertas para cerrarlas cuando llegue un cp
//	ArrayDeque<Symbol> openedTagsStac;
//	
//	
	/** @param Fichero fuente 
	 *  @param Fichero donde se genera el código */
	public SyntacticAnalyzer(String path, String output) {
		gen = new CodeGenerator(output);	
		lex = new LexicalAnalyzer();
		sem = new SemanticAnalyzer(lex);
		lex.start(path);
	}
	
	/** Inicia el procesado del fuente */
	public void start() {
		analyzeProgram();
	}
	
	/** Muestra un mensaje de error sintáctico */
	private void printSyntacticError(String s) {
		Symbol saux = lex.next();
		System.err.println("Syntactic error on line "+lex.getLineNumber()+". Found: "+saux.val()+"; Expected: "+s);
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
		gen.startHead();
			
		analyze(NT.IMPORTS);
		analyze(NT.META);
		analyze(NT.DEFINES);
		
		// Genera la etiqueta de cierre del head y apertura del body
		gen.startBody();
		
		analyze(NT.TAG);
		
		if(lex.nextAndUndo().sym() != Lexer.__end) {
			printSyntacticError("$ program");
			return false;
		}
		
		// Genera las etiquetas de cierre html
		gen.end();
		
		return true;
	}
	
	/** <IMPORTS> -> import dp <VALS> pc <IMPORTS> | $ */
	private void analyzeImports() {	
		Symbol s = lex.nextAndUndo();
		analyze(Lexer._import, false);
		analyze(Lexer.__dp, false);	
		Symbol vals[] = analyze(NT.VALS);
		sem.validate(s, vals);
		analyze(Lexer.__pc, false);
		gen.genImport(vals[0].val());
		analyze(NT.IMPORTS);
	}
	
	/** <META> -> meta dp <VALS> pc <META> | $*/
	private void analyzeMeta() {
		Symbol s = lex.nextAndUndo();
		analyze(Lexer.__meta, false);
		analyze(Lexer.__dp, false);
		Symbol vals[] = analyze(NT.VALS);
		sem.validate(s,  vals);
		analyze(Lexer.__pc, false);
		String mdlist[] = new String[vals.length+1];
		mdlist[0] = s.val();
		for(int i=0; i<vals.length; i++) mdlist[i+1] = vals[i].val();
		gen.genMetadata(mdlist);
		analyze(NT.META);
	}
		
	/** <DEFINES> -> define dp <VALS> pc <DEFINES> | $ */
	private void analyzeDefines() {
		Symbol s = lex.nextAndUndo();
		analyze(Lexer._define, false);
		analyze(Lexer.__dp, false);
		Symbol vals[] = analyze(NT.VALS);
		sem.validate(s,  vals);
		analyze(Lexer.__pc, false);
		analyze(NT.DEFINES);
	}
	
	/** <TAGS> -> <TAG> <TAGS> | $
	 * <TAG> -> <CONTAINER> | <TABLE> | <COMPONENT> | <INCLUDE> */
	private void analyzeTags() {
		if(analyze(NT.CONTAINER)!=null);
		else if(analyze(NT.COMPONENT)!=null);
		else if(analyze(NT.TABLE)!=null);
		else if(analyze(NT.DEFINES)!=null);
		else printSyntacticError("TAGS");
		analyze(NT.TAG);
	}
	
	/** <CONTAINER> -> container dp text? op <ATTRS> <TAGS> cp */
	private void analyzeContainer() {
		Symbol s = lex.nextAndUndo();
		gen.openTag(s.val());
		analyze(Lexer.__container, false);
		analyze(Lexer.__dp, false);
		Symbol stext = (lex.nextAndUndo().sym().compareTo(Lexer.__text)==0) ? lex.next() : null;
		if(stext!=null) { gen.genAttrs(Lexer.__text, stext.val());}
		analyze(Lexer.__op, false);
		Symbol vals[] = analyze(NT.ATTRS);
		sem.validate(s, vals);
		gen.finishOpenTag();
		analyze(NT.TAG);
		analyze(Lexer.__cp, false);
		gen.closeTag();
	}
	
	/** <TABLE> -> table dp op <ATTRS> <HROW> <ROWS> cp */
	private void analyzeTable() {
		Symbol s = lex.nextAndUndo();
		gen.openTag(s.val());
		analyze(Lexer._table, false);
		analyze(Lexer.__dp, false);
		analyze(Lexer.__op, false);
		Symbol vals[] = analyze(NT.ATTRS);
		sem.validate(s, vals);
		gen.finishOpenTag();
		analyze(NT.HROW);
		analyze(NT.ROWS);
		analyze(Lexer.__cp, false);
		gen.closeTag();
	}
	
	/** <HROW> -> hrow dp op <ATTRS> <TAGS> cp | $ */
	private void analyzeHrow() {
		Symbol s = lex.nextAndUndo();
		gen.openTag(s.val());
		analyze(Lexer._hrow, false);
		analyze(Lexer.__dp, false);
		analyze(Lexer.__op, false);
		Symbol vals[] = analyze(NT.ATTRS);
		sem.validate(s, vals);
		gen.finishOpenTag();
		analyze(NT.TAG);
		analyze(Lexer.__cp, false);
		gen.closeTag();
	}
	
	/** <ROWS> -> row dp op <ATTRS> <TAGS> cp <ROWS> | $ */
	private void analyzeRows() {
		Symbol s = lex.nextAndUndo();
		gen.openTag(s.val());
		analyze(Lexer._row, false);
		analyze(Lexer.__dp, false);
		analyze(Lexer.__op, false);
		Symbol vals[] = analyze(NT.ATTRS);
		sem.validate(s, vals);
		gen.finishOpenTag();
		analyze(NT.TAG);
		analyze(Lexer.__cp, false);
		gen.closeTag();
		analyze(NT.ROWS);
	}
	
	/** <COMPONENT> -> component dp text? (op <ATTRS> cp)? */
	private void analyzeComponent() {
		Symbol s = lex.nextAndUndo();
		gen.openTag(s.val());
		analyze(Lexer.__component, false);
		analyze(Lexer.__dp, false);
		Symbol stext = (lex.nextAndUndo().sym().compareTo(Lexer.__text)==0) ? lex.next() : null;
		if(stext!=null) { gen.genAttrs(Lexer.__text, stext.val());}
		if(analyze(Lexer.__op, true)!=null) {
			Symbol vals[] = analyze(NT.ATTRS);
			sem.validate(s, vals);
			analyze(Lexer.__cp, false);
		}else {
			lex.undo();
			analyze(Lexer.__pc, false);
		}
		gen.finishOpenTag();
		gen.closeTag();
	}
	
	/** <INCLUDE> -> include dp <VALS> pc */
	private void analyzeIncludes() {
		Symbol s = lex.nextAndUndo();
		analyze(Lexer._include, false);
		analyze(Lexer.__dp, false);
		Symbol vals[] = analyze(NT.VALS);
		sem.validate(s, vals);
		analyze(Lexer.__pc, false);
	}
	
	/** <ATTRS> -> attr dp <VALS> pc <ATTRS> | $ */
	private Symbol[] analyzeAttrs() {
		Symbol at = lex.nextAndUndo();
		analyze(Lexer.__attr, false);
		analyze(Lexer.__dp, false);
		Symbol v[] = analyze(NT.VALS);
		sem.validate(at, v);
		analyze(Lexer.__pc, false);
		gen.genAttrs(at.val(), v);
		Symbol recAttrs[] = analyze(NT.ATTRS);
		Symbol attrs[] = new Symbol[(recAttrs==null ? 1 : recAttrs.length+1)];
		for(int i=1; i<attrs.length; i++) attrs[i] = recAttrs[i-1];
		attrs[0] = at;
		
		return attrs;
	}
	
	/** <VALS> -> <VAL> | <VAL> <VALS>
	 * <VAL> -> val | color | text | var | int | id | measure | bool */
	private Symbol[] analyzeVals() {
		Symbol ss = lex.nextAndUndo();
		analyze(Lexer.__val, false);
		Symbol[] recVals = analyze(NT.VALS);
		Symbol values[] = new Symbol[(recVals==null ? 1 : recVals.length+1)];
		for(int i=1; i<values.length; i++) values[i] = recVals[i-1];
		values[0] = ss;
		
		return values;
	}
	
	
	/** Analiza un terminal, el terminal puede ser o no anulable */
	private Symbol analyze(String terminal, boolean nullable) {
		Symbol ns = lex.next();
		String next = ns.sym();
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
			return null;
		}	
		return ns;
	}
	
	/** Analiza un no terminal */
	private Symbol[] analyze(NT nonterminal) {
		boolean match = first(nonterminal, lex.nextAndUndo().sym());
		Symbol notnull[] = new Symbol[0];
		switch(nonterminal) {
		case META:
			if(match) {	analyzeMeta(); return notnull; } else { return null; }
		case IMPORTS:
			if(match) {	analyzeImports(); return notnull; } else { return null; }
		case DEFINES:
			if(match) { analyzeDefines(); return notnull; } else { return null; }
		case TAG:
			if(match) {	analyzeTags(); return notnull; } else { return null; }
		case CONTAINER:
			if(match) {	analyzeContainer(); return notnull; } else { return null; }
		case COMPONENT:
			if(match) {	analyzeComponent(); return notnull; } else { return null; }
		case INCLUDE:
			if(match) {	analyzeIncludes(); return notnull; } else { return null; }
		case ATTRS:
			if(match) {	return analyzeAttrs(); } else { return null; }
		case VALS:
			if(match) {	return analyzeVals(); } else { return null; }
		case TABLE:
			if(match) {	analyzeTable(); return notnull; } else { return null; }
		case HROW:
			if(match) {	analyzeHrow(); return notnull; } else { return null; }
		case ROWS:
			if(match) {	analyzeRows(); return notnull; } else { return null; }
		default: System.out.println("ERROR"); return null;
		}
	}
	
	
	public void print(String s) {
		System.out.println(s);
	}
	
}
