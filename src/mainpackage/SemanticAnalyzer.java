package mainpackage;

import java.util.Arrays;
import java.util.Hashtable;

public class SemanticAnalyzer {

	LexicalAnalyzer lex;
	private Symbol currentAttr;
	
	public SemanticAnalyzer(LexicalAnalyzer lex) {
		this.lex = lex;
	}
	
	public boolean validate(Symbol attr, Symbol[] val) {
		int quantvals[] = {1, 2, 4};
		int tdecorvals[] = {1, 2, 3, 4, 5};
		
		currentAttr = attr;
		
		switch(attr.val()) {
		case Lexer._import:
			return validate(1, val, Lexer.__text);
		case Lexer._author:
			return validate(1, val, Lexer.__text);
		case Lexer._charset:
			return validate(1, val, Lexer.__text);
		case Lexer._description:
			return validate(1, val, Lexer.__text);
		case Lexer._keywords:			
			return validate(1, val, Lexer.__text);
		case Lexer._lang:
			return validate(1, val, Lexer.__text);
		case Lexer._pageicon:
			return validate(1, val, Lexer.__text);
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
			return validate(-1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._class, Lexer._effect, Lexer._elevation, Lexer._height,
					Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, Lexer._width);
		case Lexer._dropdown:
			return validate(-1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._class, Lexer._dropdowntype, Lexer._effect, Lexer._elevation,
					Lexer._height, Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, 
					Lexer._width);
		case Lexer._hbox:
			return validate(-1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._class, Lexer._effect, Lexer._elevation, Lexer._height,
					Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, Lexer._spacing,
					Lexer._width);
		case Lexer._modal:
			return validate(-1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._class, Lexer._effect, Lexer._elevation, Lexer._height,
					Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, Lexer._width);
		case Lexer._sidebar:
			return validate(-1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._collapsible, Lexer._class, Lexer._effect, Lexer._elevation, 
					Lexer._height, Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, 
					Lexer._sidebartype, Lexer._width);
		case Lexer._tabbedbox:
			return validate(-1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._class, Lexer._effect, Lexer._elevation, Lexer._height,
					Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, Lexer._tabcolor,
					Lexer._width);
		case Lexer._table:
			return validate(-1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._class, Lexer._effect, Lexer._elevation, Lexer._height,
					Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, Lexer._width);
		case Lexer._vbox:
			return validate(-1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor,
					Lexer._borderradius, Lexer._class, Lexer._effect, Lexer._elevation, Lexer._height,
					Lexer._id, Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, Lexer._spacing,
					Lexer._width);
		case Lexer._audio:
			return validate(-1, val, Lexer._autoplay, Lexer._controls, Lexer._height, Lexer._loop,  Lexer._muted,
							Lexer._preload, Lexer._src, Lexer._tooltip, Lexer._width);
		case Lexer._button:
			return validate(-1, val, Lexer._animation,  Lexer._bgcolor,  Lexer._border,  Lexer._bordercolor, 
							Lexer._borderradius,  Lexer._class,  Lexer._effect,  Lexer._elevation, Lexer._fontfamily,
							Lexer._fontsize, Lexer._height,	Lexer._id,  Lexer._link,  Lexer._margin,  Lexer._onclick,
							Lexer._padding,  Lexer._textalign, Lexer._textcolor, Lexer._textdecoration,  Lexer._tooltip,
							Lexer._width);
		case Lexer._checkbox:
			return validate(-1, val, Lexer._animation,  Lexer._bgcolor,  Lexer._border,  Lexer._bordercolor, 
					Lexer._borderradius,  Lexer._class,  Lexer._effect,  Lexer._elevation, Lexer._fontfamily, 
					Lexer._fontsize, Lexer._height, Lexer._id,  Lexer._link,  Lexer._margin,  Lexer._onclick, 
					Lexer._padding,  Lexer._textalign, Lexer._textcolor, Lexer._textdecoration,  Lexer._tooltip, 
					Lexer._width);
		case Lexer._image:
			return validate(-1, val, Lexer._alt,  Lexer._animation, Lexer._bgcolor,  Lexer._border, Lexer._bordercolor,  
					Lexer._borderradius,  Lexer._class,  Lexer._effect,  Lexer._elevation,  Lexer._height,  Lexer._id, 
					Lexer._link,  Lexer._margin, Lexer._onclick,  Lexer._padding,  Lexer._src,  Lexer._tooltip,  Lexer._width);
		case Lexer._label:
			return validate(-1, val, Lexer._animation,  Lexer._bgcolor,  Lexer._border,  Lexer._bordercolor, Lexer._borderradius, 
					Lexer._class,  Lexer._effect,  Lexer._elevation, Lexer._fontfamily, Lexer._fontsize, Lexer._height,  Lexer._id, 
					Lexer._link,  Lexer._margin, Lexer._onclick, Lexer._padding,  Lexer._textalign,  Lexer._textcolor, 
					Lexer._textdecoration,  Lexer._tooltip,	Lexer._width);
		case Lexer._radiobutton:
			return validate(-1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor, Lexer._borderradius, 
					Lexer._class, Lexer._effect, Lexer._elevation, Lexer._fontfamily, Lexer._fontsize, Lexer._height, Lexer._id, 
					Lexer._link, Lexer._margin, Lexer._onclick, Lexer._padding, Lexer._radiogroup, Lexer._textalign, Lexer._textcolor, 
					Lexer._textdecoration, Lexer._tooltip, Lexer._width);
		case Lexer._textfield:
			return validate(-1, val, Lexer._animation, Lexer._bgcolor, Lexer._border, Lexer._bordercolor, Lexer._borderradius, 
					Lexer._class, Lexer._effect, Lexer._elevation, Lexer._fontfamily, Lexer._fontsize, Lexer._height, Lexer._id, 
					Lexer._link, Lexer._margin, Lexer._onchange, Lexer._onclick, Lexer._padding, Lexer._placeholder, Lexer._textalign, 
					Lexer._textcolor, Lexer._textdecoration, Lexer._tooltip, Lexer._width);
		case Lexer._video:
			return validate(-1, val, Lexer._animation, Lexer._autoplay, Lexer._bgcolor, Lexer._border, Lexer._bordercolor, 
					Lexer._borderradius,  Lexer._class, Lexer._controls, Lexer._effect, Lexer._elevation, Lexer._height, 
					Lexer._id, Lexer._link, Lexer._loop, Lexer._margin, Lexer._muted, Lexer._onclick, Lexer._padding, 
					Lexer._poster, Lexer._preload, Lexer._src, Lexer._tooltip, Lexer._width);
		case Lexer._align:
			return validate(1, val, Lexer._top, Lexer._right, Lexer._bottom, Lexer._left, Lexer._topright, Lexer._topleft, 
					Lexer._bottomright, Lexer._bottomleft, Lexer._center);
		case Lexer._alt:
			return validate(1, val, Lexer.__text);
		case Lexer._animation:
			return validate(1, val,  Lexer._zoom,  Lexer._fading,  Lexer._fadein, Lexer._spin,  Lexer._moveup, 
					 Lexer._moveright,  Lexer._movedown,  Lexer._moveleft);
		case Lexer._autoplay:
			return validate(1, val, Lexer._true, Lexer._false);
		case Lexer._bgcolor:
			return validate(1, val, Lexer.__color);
		case Lexer._border:
			return validate(quantvals, val, Lexer.__measure);
		case Lexer._bordercolor:
			return validate(1, val, Lexer.__color);
		case Lexer._borderradius:
			return validate(quantvals, val, Lexer.__measure);
		case Lexer._class:
			return validate(1, val, Lexer.__text);
		case Lexer._collapsible:
			return validate(1, val, Lexer._true, Lexer._false);
		case Lexer._controls:
			return validate(1, val, Lexer._true, Lexer._false);
		case Lexer._dropdowntype:
			return validate(1, val, Lexer._clickable, Lexer._hoverable);
		case Lexer._effect:
			return validate(1, val, Lexer._opacity,Lexer._opacitymin, Lexer._opacitymax, Lexer._sepia, Lexer._sepiamin, 
					Lexer._sepiamax, Lexer._grayscale, Lexer._grayscalemin, Lexer._grayscalemax);
		case Lexer._elevation:
			return validate(1, val, Lexer.__integer);
		case Lexer._fontfamily:
			return validate(1, val, Lexer.__text);
		case Lexer._fontsize:
			return validate(1, val, Lexer.__measure);
		case Lexer._height:
			return validate(1, val, Lexer.__measure);
		case Lexer._id:
			return validate(1, val, Lexer.__text);
		case Lexer._link:
			return validate(1, val, Lexer.__text);
		case Lexer._loop:
			return validate(1, val, Lexer._true, Lexer._false);
		case Lexer._margin:
			return validate(quantvals, val, Lexer.__measure);
		case Lexer._muted:
			return validate(1, val, Lexer._true, Lexer._false);
		case Lexer._onchange:
			return validate(1, val, Lexer.__text);
		case Lexer._onclick:
			return validate(1, val, Lexer.__text);
		case Lexer._padding:
			return validate(quantvals, val, Lexer.__measure);
		case Lexer._placeholder:
			return validate(1, val, Lexer.__text);
		case Lexer._poster:
			return validate(1, val, Lexer.__text);
		case Lexer._preload:
			return validate(1, val, Lexer._true, Lexer._false);
		case Lexer._radiogroup:
			return validate(1, val, Lexer.__text);
		case Lexer._selected:
			return validate(1, val, Lexer._true, Lexer._false);
		case Lexer._sidebartype:
			return validate(1, val, Lexer._floating, Lexer._sliding);
		case Lexer._src:
			return validate(1, val, Lexer.__text);
		case Lexer._spacing:
			return validate(1, val, Lexer.__measure);
		case Lexer._tabcolor:
			return validate(1, val, Lexer.__color);
		case Lexer._tabs:
			return validate(-1, val, Lexer.__text);
		case Lexer._textalign:
			return validate(1, val, Lexer._right, Lexer._left, Lexer._center);
		case Lexer._textcolor:
			return validate(1, val, Lexer.__color);
		case Lexer._textdecoration:
			return validate(tdecorvals, val, Lexer._bold, Lexer._italic, Lexer._underline, Lexer._overline, Lexer._strikethrough);
		case Lexer._tooltip:
			return validate(1, val, Lexer.__text);
		case Lexer._width:
			return validate(1, val, Lexer.__measure);
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
		// nvals=-1 significa que puede haber un número indefinido de parámetros
		wrongNVals = (nvals[0] == -1) ? false : wrongNVals;
		if(wrongNVals) {
			wrongNumberOfValues();
			return false;
		}
		
		for(Symbol sy : vals) {
			boolean wrongSym = true;
			for(String str : lexparams) {
				if(sy.sym()==Lexer.__meta || sy.sym()==Lexer.__container || sy.sym()==Lexer.__val ||
						sy.sym()==Lexer.__component || sy.sym()==Lexer.__attr ) {
					if(sy.val().compareTo(str) == 0) {
						wrongSym = false;
					}
				}else {
					if(sy.sym().compareTo(str) == 0) {
						wrongSym = false;
					}
				}
				
			}
			if(wrongSym) {
				System.out.println("Error on "+sy.val()+" - "+sy.sym());
				printSemanticError();
				return false;
			}
		}
		
		return true;
	}
	
	/** Imprime un mensaje de error: Número de parametros inválido */
	private void wrongNumberOfValues(){
		System.err.println("Semantic error on line "+lex.getLineNumber()+". Too many values.");
	}
	
	/** Imprime un mensaje de error: Error semántico */
	private void printSemanticError() {
		System.err.println("Semantic error on line "+lex.getLineNumber()+". "+currentAttr.val());
	}
	
	private void print(Symbol s, Symbol vals[]) {
		System.out.print("Attr: "+s.val()+"; Vals:");
		for(Symbol sym : vals) {
			System.out.print(" "+sym.val());
		}
		System.out.println();
	}
}
