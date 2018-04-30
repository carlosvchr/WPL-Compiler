package mainpackage;

public class SemanticAnalyzer {

	public boolean validate(Symbol attr, Symbol[] val) {
		
		switch(attr.val()) {
		case Lexer._author:				// vv
		case Lexer._description:		// vv
		case Lexer._title:				// vv
		case Lexer._pageicon:			// vv
		case Lexer._keywords:			// vv
		case Lexer._lang:
			if(val.length != 1) {
				printTooMuchValuesError();
				return false;
			}else if(val[0].sym().compareTo(Lexer.__text) != 0) {
				printSemanticError();
				return false;
			}else {
				return true;
			}
		case Lexer._redirect:
			if(val.length != 2) {
				printTooMuchValuesError();
				return false;
			}else if(val[0].sym().compareTo(Lexer.__text) != 0 || val[1].sym().compareTo(Lexer.__integer) != 0) {
				printSemanticError();
				return false;
			}else{
				return true;
			}
		case Lexer._charset:
			if(val.length != 1) {
				printTooMuchValuesError();
				return false;
			}else if(val[0].sym().compareTo(Lexer._charset) != 0) {
				printSemanticError();
				return false;
			}else {
				return true;
			}
		case Lexer._import:
			if(val.length != 1) {
				printTooMuchValuesError();
				return false;
			}else if(val[0].sym() != Lexer.__text) {
				printSemanticError();
				return false;
			}else {
				return true;
			}
		case Lexer._hbox:
			if(val.length != 1) {
				printTooMuchValuesError();
				return false;
			}else if(val[0].sym().compareTo(Lexer._align) != 0 || val[0].sym().compareTo(Lexer._animation) != 0 || 
					val[0].sym().compareTo(Lexer._bgcolor) != 0 || val[0].sym().compareTo(Lexer._border) != 0 ||
					val[0].sym().compareTo(Lexer._bordercolor) != 0 ||  val[0].sym().compareTo(Lexer._borderradius) != 0 ||
					val[0].sym().compareTo(Lexer._class) != 0 || val[0].sym().compareTo(Lexer._effect) != 0 ||
					val[0].sym().compareTo(Lexer._elevation) != 0 || val[0].sym().compareTo(Lexer._id) != 0 ||
					val[0].sym().compareTo(Lexer._height) != 0 || val[0].sym().compareTo(Lexer._link) != 0 ||
					val[0].sym().compareTo(Lexer._margin) != 0 || val[0].sym().compareTo(Lexer._padding) != 0 ||
					val[0].sym().compareTo(Lexer._tooltip) != 0 || val[0].sym().compareTo(Lexer._width) != 0) {
				printSemanticError();
				return false;
			}else {
				return true;
			}
	
		}

		return false;
	}
	
	
	private void printTooMuchValuesError(){
		System.err.println("Semantic Analyzer: Too much values.");
	}
	
	private void printSemanticError() {
		System.err.println("Semantic Analyzer: Error ");
	}
}
