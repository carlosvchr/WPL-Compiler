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
			}else if(val[0].sym().compareTo(Lexer._text) != 0) {
				printSemanticError();
				return false;
			}else {
				return true;
			}
		case Lexer._redirect:
			if(val.length != 2) {
				printTooMuchValuesError();
				return false;
			}else if(val[0].sym().compareTo(Lexer._text) != 0 || val[1].sym().compareTo(Lexer._integer) != 0) {
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
			}else if(val[0].sym() != Lexer._text) {
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
