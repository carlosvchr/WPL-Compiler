package mainpackage;

/**
 * En esta clase se almacena el símbolo generado por el analizador
 * léxico junto con el valor asociado. El analizador semántico hará
 * uso de este valor para realizar comprobaciones de tipo.
 * 
 * */

public class Symbol {

	private String symbol;
	private String value;
	
	public Symbol(String symbol, String value) {
		this.symbol = symbol;
		this.value = value;
	}
	
	/** Obtiene el símbolo */
	public String sym() {
		return this.symbol;
	}
	
	/** Obtiene el valor asociado al símbolo */
	public String val() {
		return this.value;
	}
	
}