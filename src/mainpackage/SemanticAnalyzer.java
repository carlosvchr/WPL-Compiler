package mainpackage;

import java.util.Arrays;
import java.util.Hashtable;

public class SemanticAnalyzer {

	Hashtable<String, String> symbolTable;
	
	public boolean validate(Symbol attr, Symbol[] val) {
		
		switch(attr.val()) {
		case Lexer._import:
			return validate(1, val, Lexer.__text);
		case Lexer._author:
		case Lexer._charset:
		case Lexer._description:
		case Lexer._keywords:			
		case Lexer._lang:
		case Lexer._pageicon:
		case Lexer._title:			
			return validate(1, val, Lexer.__text);
		case Lexer._redirect:
			if(val.length == 2) {
				if(validate(1, Arrays.copyOfRange(val, 0, 1), Lexer.__integer))
					return validate(1, Arrays.copyOfRange(val, 1, 2), Lexer.__text);				
			}else return false;
		case Lexer._define:
			if(val.length == 2) {
				if(validate(1, Arrays.copyOfRange(val, 0, 1), Lexer.__var))
					return validate(1, Arrays.copyOfRange(val, 1, 2), Lexer.__text);				
			}else return false;
		case Lexer._accordion:
			return validate(1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._class, Lexer._effect, Lexer._elevation, Lexer._height,
					Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, Lexer._width);
		case Lexer._dropdown:
			return validate(1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._class, Lexer._dropdowntype, Lexer._effect, Lexer._elevation,
					Lexer._height, Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, 
					Lexer._width);
		case Lexer._hbox:
			return validate(1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._class, Lexer._effect, Lexer._elevation, Lexer._height,
					Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, Lexer._spacing,
					Lexer._width);
		case Lexer._modal:
			return validate(1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._class, Lexer._effect, Lexer._elevation, Lexer._height,
					Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, Lexer._width);
		case Lexer._sidebar:
			return validate(1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._collapsible, Lexer._class, Lexer._effect, Lexer._elevation, 
					Lexer._height, Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, 
					Lexer._sidebartype, Lexer._width);
		case Lexer._tabbedbox:
			return validate(1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._class, Lexer._effect, Lexer._elevation, Lexer._height,
					Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, Lexer._tabcolor,
					Lexer._width);
		case Lexer._table:
			return validate(1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._class, Lexer._effect, Lexer._elevation, Lexer._height,
					Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, Lexer._width);
		case Lexer._vbox:
			return validate(1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._class, Lexer._effect, Lexer._elevation, Lexer._height,
					Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, Lexer._spacing,
					Lexer._width);
		}

		return false;
	}
	
	/** Check if the values or attributes are valid for their containers */
	private boolean validate(int nvals, Symbol vals[], String... lexparams) {
		int nv[] = new int[1];
		nv[0] = nvals;
		return validate(nv, vals, lexparams);
	}
	
	/** Check if the values or attributes are valid for their containers */
	private boolean validate(int nvals[], Symbol vals[], String... lexparams) {
		boolean wrongNVals = true;
		for(int i : nvals) {
			if(vals.length == i) wrongNVals = false;
		}
		if(wrongNVals) {
			wrongNumberOfValues();
			return false;
		}
		
		for(Symbol sy : vals) {
			boolean wrongSym = true;
			for(String str : lexparams) {
				if(sy.sym().compareTo(str) == 0) {
					wrongSym = false; break;
				}
			}
			if(wrongSym) {
				printSemanticError();
				return false;
			}
		}
		
		return true;
	}
	
	/** Agrega una variable a la tabla de símbolos */
	public void addSymbol(String key, String type) {
		symbolTable.put(key, type);
	}
	
	/** Imprime un mensaje de error: Número de parametros inválido */
	private void wrongNumberOfValues(){
		System.err.println("Semantic Analyzer: Too much values.");
	}
	
	/** Imprime un mensaje de error: Error semántico */
	private void printSemanticError() {
		System.err.println("Semantic Analyzer: Error ");
	}
}
