package mainpackage;

/**
 * En esta clase se almacena el símbolo generado por el analizador
 * léxico junto con el valor asociado. El analizador semántico hará
 * uso de este valor para realizar comprobaciones de tipo.
 * 
 * */

class Symbol {

	private String symbol;
	private String value;
	private long line;
	
//	public Symbol(String symbol, String value) {
//		this.symbol = symbol;
//		this.value = value;
//	}
	
	public Symbol(String symbol, String value, long line) {
		this.symbol = symbol;
		this.value = value;
		this.line = line;
	}
	
	/** Obtiene el símbolo */
	public String sym() {
		return this.symbol;
	}
	
	/** Obtiene el valor asociado al símbolo */
	public String val() {
		return this.value;
	}
	
	/** Modifica el valor del símbolo */
	public void setVal(String v) {
		this.value = v;
	}
	
	/** Modifica el identificador del símbolo */
	public void setSym(String v) {
		this.symbol = v;
	}
	
	/** Devuelve la linea donde se ha leido el simbolo */
	public long getLine() {
		return this.line;
	}
	
}
