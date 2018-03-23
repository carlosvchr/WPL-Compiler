package mainpackage;

import java.util.regex.*;

public class REManager {
	
	private static Pattern pattern;
	
	/** Utilizamos la clase predeterminada de java que analiza
	 * expresiones regulares para buscar una coincidencia */
	public static String validate(String re, String exp) {		
		pattern = Pattern.compile(re);
		Matcher matcher = pattern.matcher(exp);
		if(matcher.find()) {
			return matcher.group();
		}
		else return null;
	}
	
}
