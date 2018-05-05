package mainpackage;

import java.util.Stack;

public class SyntacticAnalyzer {
	
	/** En esta clase se procede con la siguiente implementación: *********************************************
	 * 
	 * <PROGRAM>			-> <IMPORTS> <META> <DEFINES> <TAGS>
	 * <IMPORTS>			-> import dp <VALS> pc <IMPORTS> | $
	 * <META>				-> (meta dp <VALS> pc)* | $
	 * <DEFINES>			-> define dp <VALS> pc <DEFINES> | $
	 *
	 * <TAGS>				-> <TAG> <TAGS> | $
	 * <TAG>				-> <CONTAINER> | <TABLE> | <COMPONENT> | <INCLUDE> | <HTML>
	 *
	 * <CONTAINER>			-> container dp text? (pc | (op <ATTRS> <TAGS> cp))
	 * <TABLE>				-> table dp op <ATTRS> <HROW> <ROWS> cp
	 * <HROW>				-> hrow dp op <ATTRS> <TAGS> cp | $
	 * <ROWS>				-> row dp op <ATTRS> <TAGS> cp <ROWS> | $
	 * <COMPONENT>			-> component dp text? (pc | (op <ATTRS> cp))
	 * <INCLUDE>			-> include dp <VALS> pc
	 * <HTML>				-> html dp op <OPEN> <TAGS> <CLOSE> cp
	 * <OPEN>				-> open dp text pc
	 * <CLOSE>				-> close dp text pc
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
	 * Debemos buscar un punto de sincronización.
	 * Para ello analizaremos los siguientes del no terminal que causó el error e iremos descartando símbolos
	 * del analizador léxico hasta encontrar alguno que coincida con alguno de los siguientes. 
	 * Entonces continuamos desde ahí el análisis.
	 * 
	 * */
	
	/** No terminales */
	private enum NT { META, IMPORTS, DEFINES, TAG, TAGS, CONTAINER, TABLE, HROW,
					ROWS, COMPONENT, INCLUDE, ATTRS, VAL, VALS, HTML, OPEN, CLOSE }
	
	// Mantenemos en memoria un indicador del ultimo no terminal desplegado para sincronizar
	Stack <NT> currentNonTerminal;
	
	boolean panicMode;
	
	// Analizador léxico al que iremos requiriendo símbolos
	LexicalAnalyzer lex = null;
//	
//	// Analizador semantico que comprobara que los valores asociados a los atributos son correctos
	SemanticAnalyzer sem = null;
//	
//	// CodeGenerator es la clase que genera el código
	CodeGenerator gen = null;

	
	/** @param Fichero fuente 
	 *  @param Fichero donde se genera el código */
	public SyntacticAnalyzer(String path, String output) {
		gen = new CodeGenerator(output);	
		lex = new LexicalAnalyzer(gen);
		sem = new SemanticAnalyzer(gen);
		lex.start(path);
		currentNonTerminal = new Stack<>();
		panicMode = false;
	}
	
	/** Inicia el procesado del fuente */
	public boolean start() {
		analyzeProgram();
		return !gen.isAborted();
	}
	
	/** Muestra un mensaje de error sintáctico */
	private void printSyntacticError(String s, long nline) {
		gen.abort();
		System.err.println("Error on line "+nline+". "+s);
		synchro();
	}
	
	/** Tras un error, trata de buscar una sincronización para continuar el procesado *
	 * @param no terminal que causó el error */
	private void synchro() {
		panicMode = true;
		boolean foundSync = false;
		Symbol s = lex.next();	// Este simbolo es el que causó el error. Lo omitimos.
		while(!foundSync) {
			s = lex.next();
			if(s != null) {
				if(following(currentNonTerminal.peek(), s.sym())) foundSync = true;
			}else {
				System.err.println("Error irrecuperable.");
				return;
			}
		}
		lex.undo();
	}
	
	/** procesos de finalización comunes de los no terminales */
	private void endFunc() {
		currentNonTerminal.pop();
		panicMode = false;
	}

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
		case HTML:
			return (t.compareTo(Lexer._html) == 0);
		case OPEN:
			return (t.compareTo(Lexer._open) == 0);
		case CLOSE:
			return (t.compareTo(Lexer._close) == 0);
		case TAGS:
		case TAG:
			return (t.compareTo(Lexer.__container) == 0 || t.compareTo(Lexer.__component) == 0 || 
					t.compareTo(Lexer._include) == 0 || t.compareTo(Lexer._table) == 0 ||
					t.compareTo(Lexer._html) == 0);
		case CONTAINER:
			return (t.compareTo(Lexer.__container) == 0);
		case COMPONENT:
			return (t.compareTo(Lexer.__component) == 0);
		case INCLUDE:
			return (t.compareTo(Lexer._include) == 0);
		case ATTRS:
			return (t.compareTo(Lexer.__attr) == 0);
		case VAL:
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
		switch (nt){
		case IMPORTS:
			return (t.compareTo(Lexer._import)==0 || t.compareTo(Lexer.__meta)==0 ||
					t.compareTo(Lexer._define)==0 || t.compareTo(Lexer.__container)==0 ||
					t.compareTo(Lexer.__component)==0 || t.compareTo(Lexer._include)==0 ||
					t.compareTo(Lexer._table)==0 || t.compareTo(Lexer._html)==0 ||
					t.compareTo(Lexer.__end)==0);
		case META:
			return (t.compareTo(Lexer.__meta)==0 ||	t.compareTo(Lexer._define)==0 || 
				t.compareTo(Lexer.__container)==0 || t.compareTo(Lexer.__component)==0 || 
				t.compareTo(Lexer._include)==0 || t.compareTo(Lexer._table)==0 || 
				t.compareTo(Lexer._html)==0 || t.compareTo(Lexer.__end)==0);
		case DEFINES:
			return (t.compareTo(Lexer._define)==0 || t.compareTo(Lexer.__container)==0 ||
					t.compareTo(Lexer.__component)==0 || t.compareTo(Lexer._include)==0 ||
					t.compareTo(Lexer._table)==0 || t.compareTo(Lexer._html)==0 ||
					t.compareTo(Lexer.__end)==0);
		case TAGS:
			return (t.compareTo(Lexer.__cp)==0 || t.compareTo(Lexer._close)==0 ||
					t.compareTo(Lexer.__end)==0);
		case CONTAINER:
		case TABLE:
		case COMPONENT:
		case INCLUDE:
		case HTML:
		case TAG:
			return (t.compareTo(Lexer.__container)==0 || t.compareTo(Lexer.__component)==0 || 
					t.compareTo(Lexer._include)==0 || t.compareTo(Lexer._table)==0 || 
					t.compareTo(Lexer._html)==0 || t.compareTo(Lexer.__cp)==0 ||
					t.compareTo(Lexer._close)==0 || t.compareTo(Lexer.__end)==0);
		case HROW:
			return (t.compareTo(Lexer.__cp)==0 || t.compareTo(Lexer._row)==0);
		case ROWS:
			return (t.compareTo(Lexer.__cp)==0 || t.compareTo(Lexer._row)==0);
		case OPEN:
			return (t.compareTo(Lexer.__container)==0 || t.compareTo(Lexer.__component)==0 || 
				t.compareTo(Lexer._include)==0 || t.compareTo(Lexer._table)==0 || 
				t.compareTo(Lexer._html)==0 || t.compareTo(Lexer._close)==0);
		case CLOSE:
			return (t.compareTo(Lexer.__cp)==0);
		case ATTRS:
			return (t.compareTo(Lexer.__attr)==0 || t.compareTo(Lexer._hrow)==0 || 
				t.compareTo(Lexer._row)==0 || t.compareTo(Lexer.__cp)==0 || 
				t.compareTo(Lexer.__container)==0 || t.compareTo(Lexer.__component)==0 || 
				t.compareTo(Lexer._include)==0 || t.compareTo(Lexer._table)==0 || 
				t.compareTo(Lexer._html)==0);
		case VALS:
		case VAL:
			return (t.compareTo(Lexer.__val)==0 || t.compareTo(Lexer.__var)==0 || 
				t.compareTo(Lexer.__text)==0 || t.compareTo(Lexer.__color)==0 || 
				t.compareTo(Lexer.__integer)==0 || t.compareTo(Lexer.__measure)==0 || 
				t.compareTo(Lexer.__bool)==0 || t.compareTo(Lexer.__identifier)==0 || 
				t.compareTo(Lexer.__pc)==0);
		default: return false;
		}
	}
	
	/** <PROGRAM> -> <IMPORTS> <META> <DEFINES> <TAG> */
	private boolean analyzeProgram() {
		
		// Genera las etiquetas de apertura html
		gen.startHead();
		
		analyze(NT.IMPORTS);
		analyze(NT.META);
		Symbol s = lex.nextAndUndo();
		if(s.sym().compareTo(Lexer._import)==0) {
			String error = "imports must be declared at the beginning of the document.";
			printSyntacticError(error, s.getLine());
		}
		analyze(NT.DEFINES);
		s = lex.nextAndUndo();
		if(s.sym().compareTo(Lexer._import)==0) {
			String error = "imports must be declared at the beginning of the document.";
			printSyntacticError(error, s.getLine());
		}else if(s.sym().compareTo(Lexer.__meta)==0) {
			String error = "metadata can only be declared at the beginning, after the imports.";
			printSyntacticError(error, s.getLine());
		}
		
		// Genera la etiqueta de cierre del head y apertura del body
		gen.startBody();
		
		analyze(NT.TAG);
		if(s.sym().compareTo(Lexer._import)==0) {
			String error = "imports must be declared at the beginning of the document.";
			printSyntacticError(error, s.getLine());
		}else if(s.sym().compareTo(Lexer.__meta)==0) {
			String error = "metadata can only be declared at the beginning, after the imports.";
			printSyntacticError(error, s.getLine());
		}else if(s.sym().compareTo(Lexer._define)==0) {
			String error = "variable definitions must be declared at the beginning of the document, after the imports and metadata.";
			printSyntacticError(error, s.getLine());
		}
		
		if(lex.nextAndUndo().sym() != Lexer.__end) {
			printSyntacticError("End of file.", lex.nextAndUndo().getLine());
			return false;
		}
		
		// Genera las etiquetas de cierre html
		gen.end();
		
		return true;
	}
	
	/** <IMPORTS> -> import dp <VALS> pc <IMPORTS> | $ */
	private void analyzeImports() {	
		currentNonTerminal.push(NT.IMPORTS);
		Symbol s = lex.nextAndUndo();
		analyze(Lexer._import, false);
		analyze(Lexer.__dp, false);
		Symbol vals[] = analyze(NT.VALS);
		sem.validate(s, vals);
		analyze(Lexer.__pc, false);
		gen.genImport(vals[0].val());
		analyze(NT.IMPORTS);	
		endFunc();
	}
	
	/** <META> -> meta dp <VALS> pc <META> | $*/
	private void analyzeMeta() {
		currentNonTerminal.push(NT.META);
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
		endFunc();
	}
		
	/** <DEFINES> -> define dp <VALS> pc <DEFINES> | $ */
	private void analyzeDefines() {
		currentNonTerminal.push(NT.DEFINES);
		Symbol s = lex.nextAndUndo();
		analyze(Lexer._define, false);
		analyze(Lexer.__dp, false);
		Symbol vals[] = analyze(NT.VALS);
		sem.validate(s,  vals);
		analyze(Lexer.__pc, false);
		analyze(NT.DEFINES);
		endFunc();
	}
	
	/** <TAGS> -> <TAG> <TAGS> | $
	 * <TAG> -> <CONTAINER> | <TABLE> | <COMPONENT> | <INCLUDE> */
	private void analyzeTags() {
		currentNonTerminal.push(NT.TAG);
		if(analyze(NT.CONTAINER)!=null);
		else if(analyze(NT.COMPONENT)!=null);
		else if(analyze(NT.TABLE)!=null);
		else if(analyze(NT.DEFINES)!=null);
		else if(analyze(NT.HTML)!=null);
		else printSyntacticError("Container or Component was expected.", lex.nextAndUndo().getLine());
		analyze(NT.TAG);
		endFunc();
	}
	
	/** <CONTAINER> -> container dp text? op <ATTRS> <TAGS> cp */
	private void analyzeContainer() {
		currentNonTerminal.push(NT.CONTAINER);
		Symbol s = lex.nextAndUndo();
		gen.openTag(s.val());
		analyze(Lexer.__container, false);
		analyze(Lexer.__dp, false);
		Symbol stext = (lex.nextAndUndo().sym().compareTo(Lexer.__text)==0 && !panicMode) ? lex.next() : null;
		if(stext!=null) { gen.genAttrs(Lexer.__text, stext.val());}
		analyze(Lexer.__op, false);
		Symbol vals[] = analyze(NT.ATTRS);
		sem.validate(s, vals);
		gen.finishOpenTag();
		analyze(NT.TAG);
		analyze(Lexer.__cp, false);
		gen.closeTag();
		endFunc();
	}
	
	/** <TABLE> -> table dp op <ATTRS> <HROW> <ROWS> cp */
	private void analyzeTable() {
		currentNonTerminal.push(NT.TABLE);
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
		endFunc();
	}
	
	/** <HROW> -> hrow dp op <ATTRS> <TAGS> cp | $ */
	private void analyzeHrow() {
		currentNonTerminal.push(NT.HROW);
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
		endFunc();
	}
	
	/** <ROWS> -> row dp op <ATTRS> <TAGS> cp <ROWS> | $ */
	private void analyzeRows() {
		currentNonTerminal.push(NT.ROWS);
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
		endFunc();
	}
	
	/** <COMPONENT> -> component dp text? (op <ATTRS> cp)? */
	private void analyzeComponent() {
		currentNonTerminal.push(NT.COMPONENT);
		Symbol s = lex.nextAndUndo();
		gen.openTag(s.val());
		analyze(Lexer.__component, false);
		analyze(Lexer.__dp, false);
		Symbol stext = (lex.nextAndUndo().sym().compareTo(Lexer.__text)==0 && !panicMode) ? lex.next() : null;
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
		endFunc();
	}
	
	/** <INCLUDE> -> include dp <VALS> pc */
	private void analyzeIncludes() {
		currentNonTerminal.push(NT.INCLUDE);
		Symbol s = lex.nextAndUndo();
		analyze(Lexer._include, false);
		analyze(Lexer.__dp, false);
		Symbol vals[] = analyze(NT.VALS);
		sem.validate(s, vals);
		analyze(Lexer.__pc, false);
		// GENERATE INCLUDES
		endFunc();
	}
	
	/** <HTML> -> html dp op <OPEN> <TAGS> <CLOSE> cp */
	private void analyzeHtml() {
		currentNonTerminal.push(NT.HTML);
		analyze(Lexer._html, false);
		analyze(Lexer.__dp, false);
		analyze(Lexer.__op, false);
		analyze(NT.OPEN);
		analyze(NT.TAG);
		analyze(NT.CLOSE);
		analyze(Lexer.__cp, false);
		endFunc();
	}
	
	/** <OPEN> -> open dp text pc */
	private void analyzeOpen() {
		currentNonTerminal.push(NT.OPEN);
		Symbol s = lex.nextAndUndo();
		analyze(Lexer._open, false);
		analyze(Lexer.__dp, false);
		Symbol v[] = analyze(NT.VALS);
		sem.validate(s, v);
		analyze(Lexer.__pc, false);
		gen.genHtml(v[0].val());
		endFunc();
	}
	
	/** <CLOSE>	-> close dp text pc */
	private void analyzeClose() {
		currentNonTerminal.push(NT.CLOSE);
		Symbol s = lex.nextAndUndo();
		analyze(Lexer._close, false);
		analyze(Lexer.__dp, false);
		Symbol v[] = analyze(NT.VALS);
		sem.validate(s, v);
		analyze(Lexer.__pc, false);
		gen.genHtml(v[0].val());
		endFunc();
	}
	
	/** <ATTRS> -> attr dp <VALS> pc <ATTRS> | $ */
	private Symbol[] analyzeAttrs() {
		currentNonTerminal.push(NT.ATTRS);
		Symbol at = lex.nextAndUndo();
		analyze(Lexer.__attr, false);
		analyze(Lexer.__dp, false);
		Symbol v[] = analyze(NT.VALS);
		sem.validate(at, v);
		analyze(Lexer.__pc, false);
		gen.genAttrs(at.val(), v);
		endFunc();
		Symbol recAttrs[] = analyze(NT.ATTRS);
		Symbol attrs[] = new Symbol[(recAttrs==null ? 1 : recAttrs.length+1)];
		for(int i=1; i<attrs.length; i++) attrs[i] = recAttrs[i-1];
		attrs[0] = at;
		
		return attrs;
	}
	
	/** <VALS> -> <VAL> | <VAL> <VALS>
	 * <VAL> -> val | color | text | var | int | id | measure | bool */
	private Symbol[] analyzeVals() {
		currentNonTerminal.push(NT.VALS);
		Symbol ss = lex.nextAndUndo();
		analyze(Lexer.__val, false);
		endFunc();
		Symbol[] recVals = analyze(NT.VALS);
		Symbol values[] = new Symbol[(recVals==null ? 1 : recVals.length+1)];
		for(int i=1; i<values.length; i++) values[i] = recVals[i-1];
		values[0] = ss;
		
		return values;
	}
	
	
	/** Analiza un terminal, el terminal puede ser o no anulable */
	private Symbol analyze(String terminal, boolean nullable) {
		if(panicMode)return null;
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
			if(!nullable) {
				lex.undo();
				String error = "Unexpected value ("+ns.val()+").";
				if(terminal.compareTo(Lexer.__dp)==0)
					error = "Expected a colon character (:) before ("+ns.val()+").";
				if(terminal.compareTo(Lexer._open)==0)
					error = "html cannot have attributes or nested tags. It was expected the tag open.";
				if(terminal.compareTo(Lexer._close)==0)
					error = "html must end with close tag.";
				if(terminal.compareTo(Lexer._row)==0 && next.compareTo(Lexer._hrow)==0)
					error = "row-header can only be at the beginning.";
				else if(terminal.compareTo(Lexer._row)==0)
					error = "table can only contains row-header and row tags.";
				if(next.compareTo(Lexer._import)==0)
					error = "Imports must be declared at the beginning of the document.";
				if(next.compareTo(Lexer.__meta)==0)
					error = "Metadata can only be declared at the beginning, after the imports.";
				if(next.compareTo(Lexer._define)==0)
					error = "Variable definitions must be declared at the beginning of the document, after the imports and metadata.";
				if(next.compareTo(Lexer.__attr)==0)
					error = "Attributes can only be declared inmediatly below the tag definition.";
				if(terminal.compareTo(Lexer.__cp)==0 && (ns.sym().compareTo(Lexer.__container)==0 || ns.sym().compareTo(Lexer.__component)==0))
					error = "Components cannot have nested tags.";
				if(terminal.compareTo(Lexer.__pc)==0 && ns.sym().compareTo(Lexer.__op)==0)
					error = "Attributes cannot have nested tags.";
				if(terminal.compareTo(Lexer.__val)==0)
					error = ns.val()+" is not a valid value.";
				
				printSyntacticError(error, ns.getLine());
			}
			return null;
		}	
		return ns;
	}
	
	/** Analiza un no terminal */
	private Symbol[] analyze(NT nonterminal) {
		if(panicMode)return null;
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
		case HTML:
			if(match) {	analyzeHtml(); return notnull; } else { return null; }
		case OPEN:
			if(match) {	analyzeOpen(); return notnull; } else { return null; }
		case CLOSE:
			if(match) {	analyzeClose(); return notnull; } else { return null; }
		default: return null;
		}
	}
	
	
	public void print(String s) {
		System.out.println(s);
	}
	
}
