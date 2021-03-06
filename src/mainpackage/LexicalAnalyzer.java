package mainpackage;

import java.io.IOException;

class LexicalAnalyzer {

	private final String OP = "op";
	private final String CP = "cp";
	private final String DP = "dp";
	private final String PC = "pc";
	private final String BR = "+";
	
	/* Como es posible que varias categorias puedan generar la
	 * misma cadena en algunos casos, se debe coger siempre la 
	 * cadena más restrictiva de todas, para ello se colocarán
	 * las categorías menos restrictivas al final*/
	
	private int tabCont;		// Contador de tabuladores para saber cuando agrupar componentes
	private Symbol history;		// Guarda el último símbolo emitido por si se necesita devolver
	private boolean resendSymbol;	// Indica si tenemos que volver a emitir el último símbolo
	private IOManager inputFile;
	private String line;
	private int closeStack;		// Pila de CP consecutivos para que emitan uno por llamada
	private int openedP;		// Permite conocer cuantos OP hay abiertos 
	private boolean emmitedFinalPC; // Indicador de que se ha emitido el PC de la ultima linea del fichero
	private CodeGenerator gen;	// Pointer to code generator to abort in case of error
	private boolean endEmmited;	// Comprueba que se ha emitido el simbolo fin de fichero
	private CompResult results;	// Redirige la salida de errores
	
	/** Debido a que el analizador sintáctico va pidiendo al analizador léxico
	 * los símbolos uno por uno, se requiere un función que procese el fuente
	 * de forma que cada vez que sea llamado devuelva el siguiente símbolo al
	 * de la llamada anterior. */
	
	public LexicalAnalyzer(CodeGenerator gen, CompResult cr) {
		this.gen = gen;
		results = cr;
	}
	
	/* El analizador léxico será implementado a través de un MDD */
	public void start(String path) 
	{
		/* El proceso consistirá en ir leyendo el flujo de entrada 
		 * encontrando coincidencias y escribiendo en un nuevo fichero
		 * las categorías léxicas que serán procesadas posteriormente
		 * por el analizador sintáctico. 
		 * 
		 * El funcionamiento de la máquina discriminadora determinista
		 * consiste en ir analizando la entrada buscando alguna coincidencias 
		 * hasta que no encuentre coincidencias, entonces se interrumpe el análisis 
		 * de la entrada y se comprueba si antes de producirse la interrupción
		 * se generaba algún símbolo, en ese caso se emite y se prosigue empezando
		 * desde el carácter que generó el error. El proceso termina de forma exisota
		 * cuando se analiza todo el código fuente sin errores.
		 * Los casos de error se pueden producir en dos ocasiones:
		 * 		1.-Cuando al producirse la interrupción, la cadena hasta el carácter
		 * 			anterior no produce ninguna coincidencia.
		 * 		2.-Cuando se analiza una línea completa no se puede encontrar coincidencia
		 * 			para todos los elementos.
		 * En caso de producirse un error, se pausará la compilación y se emitirá un mensaje
		 * de error léxico y en qué línea se ha producido.*/
		
		// Creamos un objeto para leer y otro para escribir ficheros.
		inputFile = new IOManager();
		tabCont = 0;	//Iniciamos el contador
		endEmmited = false;
		
		try {
			// Abrimos el archivo en modo lectura
			inputFile.openForRead(path, false);
		} catch (IOException e) {
			System.err.println("Error when opening source file.");
			e.printStackTrace();
		}
		
		// Inicializamos la primera linea
		try {
			line = inputFile.getLine();
			if(line != null) {
				while(line.trim().endsWith(Lexer.__br)) {
					String nl = inputFile.getLine();
					if(nl != null) {
						line = trimEnd(line);	// quitamos espacios finales de linea
						line = line.substring(0, line.length()-1);	// Eliminamos el símbolo de br 
						line += nl.trim();
					}else { break; }
				}
			}
		} catch (IOException e) {
			System.err.println("Error, empty file.");
			e.printStackTrace();
		}
		closeStack = 0;
		openedP = 0;
		emmitedFinalPC = false;
	}
	
	
	public Symbol next() {
		// Primero comprobaremos si hay algún símbolo que deba ser emitido de nuevo
		if(resendSymbol) {
			resendSymbol = false;
			return history;
		}
		
		// Si tenemos en pila algún cierre de bloque, lo emitimos antes de seguir
		if(closeStack > 0) {
			closeStack--;
			openedP--;
			history = new Symbol(CP, "cp", inputFile.getLineNumber());
			return history;
		}
		
		// Si aun queda línea por procesar, no necesitamos leer del fichero
		if(line.length() > 0) {
			/* Para seguir la estrategia avariciosa, vamos a ir comparando la entrada con las
			 * distintas categorías léxicas. Las categorías léxicas se van a comparar con la 
			 * cadena entera, lo único que se exige es que la línea empiece por una categoría, 
			 * da igual como termine. La que mayor match haga es la que se emite, y se restan 
			 * del principio de la línea tantos caracteres como se hayan procesado y se repite
			 * hasta que no queden caracteres en la cadena.*/
			
			int maxMatch = 0;
			int index = -1;
			String bestMatch = "";
			
			for(int i=0; i<Lexer.lexer.length; i++) {
				int matchLen = 0;			
				String match = REManager.validate(Lexer.lexer[i][1], line);
				if(match != null) {
					matchLen = match.length();
				}
				// Si encontramos un match mayor al máximo actual lo sustituimos
				if(matchLen > maxMatch) {
					maxMatch = matchLen;
					bestMatch = match;
					index = i;
				}
			}
			
			// Comprobamos si ha habido errores
			if(index == -1) {
				/* Si no hay ninguna coincidencia significa que se ha producido 
				 * un error léxico. Debemos imprimir un mensaje de error y pasar 
				 * a la siguiente línea con el objetivo de encontrar todos los 
				 * errores en una sola compilación*/
				
				// Imprimimos error y abortamos la generación de código
				gen.abort();
				System.err.println("Error on line "+inputFile.getLineNumber()+". Unexpected input ("+line+")");
				results.add("Error on line "+inputFile.getLineNumber()+". Unexpected input ("+line+")");
				
				// Saltamos la línea actual
				line = "";
				
				// Continuamos con la siguiente línea
				return next();
			}
			
			// Si se ha producido coincidencia, restamos de la cadena la parte procesada
			line = line.substring(maxMatch, line.length());
			
			// Los comentarios los descartamos
			if(Lexer.lexer[index][0].compareTo(Lexer.__comment) == 0) {
				return next();
			}
			
			// Los espacios los descartamos, entonces devolvemos la siguiente ocurrencia
			if(Lexer.lexer[index][0].compareTo(Lexer.__blank) == 0) {
				return next();
			}
			
			// Soporte de iconos
			if(Lexer.lexer[index][0].compareTo(Lexer.__text) == 0) {
				String icons;
				while((icons=REManager.validate(Lexer.__icon, bestMatch))!=null){
					String icname = icons.substring(1, icons.length()-1).trim().replace(" ", "_");
					bestMatch = bestMatch.replace(icons, "<i class=\"material-icons\">"+icname+"</i>");
				}
			}
			
			// Por último retornamos el símbolo de la categoría que hizo mejor coincidencia
			history = new Symbol(Lexer.lexer[index][0], bestMatch, inputFile.getLineNumber());
			return history;
		
		}else { // En caso contrario, cargamos la siguiente línea
			try {
				// Cuando cargamos la linea comprobamos que no haya ningun break
				String cline = inputFile.getLine();
				
				if(cline != null) {
					while(cline.trim().endsWith(Lexer.__br)) {
						String nl = inputFile.getLine();
						if(nl != null) {
							cline = trimEnd(cline);	// quitamos espacios finales de linea
							cline = cline.substring(0, cline.length()-1);	// Eliminamos el símbolo de br 
							cline += nl.trim();
						}else { break; }
					}
				}
				
				// Comprobamos que queden lineas por leer
				if(cline == null) {
					// Si no quedan lineas devolvemos el PC de la ultima linea
					if(!emmitedFinalPC) {
						emmitedFinalPC = true;
						// Comprobamos que no se haya emitido ya un PC
						if(history.sym().compareTo(PC) != 0) {
							history = new Symbol(PC, "pc", inputFile.getLineNumber());
							return history;
						}
					}
					// Y ahora vamos devolviendo los CP que esten abiertos
					if(openedP > 0) {
						openedP--;
						history = new Symbol(CP, "cp", inputFile.getLineNumber());
						return history;
					}else if(!endEmmited){
						endEmmited = true;
						history = new Symbol(Lexer.__end, Lexer.__end, inputFile.getLineNumber());
						return history;
					}else {
						return null;
					}
				}
				// Si es distinto de null, entonces leemos la linea
				cline = trimEnd(cline);
				boolean finishLine = false;
				while(!finishLine) {
					if(trimEnd(cline).endsWith(BR)) {
						// Mientras haya breaks, se van uniendo las lineas (quitando el caracter BR)
						line += cline.substring(0, cline.length()-1);
						cline = inputFile.getLine();
						// Verificacion por si llegamos a final de fichero
						if(cline == null) {
							finishLine = true;
						}
					}else {
						finishLine = true;
					}
					
				}
				// Finalmente agregamos la ultima linea que no llevara BR
				line += cline;
			}catch(IOException ioe) {
				ioe.printStackTrace();
			}
			
			/* Lo primero es sustituir los tabuladores por conjuntos de 4 espacios 
			 * Ya que es válido que el programador escriba tanto tabuladores como 
			 * conjuntos de cuatro espacios*/
			line = line.replaceAll("\\t", "    ");
			
			// Eliminamos líneas vacías
			if (line.replaceAll(" ", "").length() == 0) {
				// Es una línea solo con espacios, entonces los borramos
				line = "";
				// Al ser una línea vacía, no la contabilizamos y devolvemos el siguiente símbolo
				return next();
			}
			
			// Aqui contamos cuantos conjuntos de 4 espacios hay al principio de la línea
			String initSpaces = REManager.validate("^ *", line);
			int ntabs = 0;
			if(initSpaces != null) {
				ntabs = initSpaces.length() / 4;
			}
			
			// Como ya hemos procesado los tabuladores, los eliminamos de la linea
			line = line.substring(initSpaces.length(), line.length());
			
			// Si es mayor, emitimos op, si es menor cp, en caso contrario pc, para separar sentencias
			if(ntabs > tabCont) {
				tabCont = ntabs;
				history = new Symbol(OP, "op", inputFile.getLineNumber());
				openedP++;
				return history;
			}else if(ntabs < tabCont) {
				// Hacemos una pila para que si hay que devolver mas cierres, se haga en las siguientes llamadas
				closeStack = tabCont - ntabs;
				tabCont = ntabs;
				history = new Symbol(PC, "pc", inputFile.getLineNumber());	// Devolvemos PC que debe ir antes de CP
				return history;
			}else {  // Si es una linea normal
				// Comprobamos que no se haya emitido ya un PC, que se emita al comienzo o despues de OP, CP o DP
				if(history != null && history.sym().compareTo(PC) != 0 && history.sym().compareTo(OP) != 0
						&& history.sym().compareTo(CP) != 0 && history.sym().compareTo(DP) != 0) {
					history = new Symbol(PC, "pc", inputFile.getLineNumber());
					return history;
				}else {
					return next();
				}
			}
		}
	}
	
	/** Devuelve el siguiente símbolo pero lo devuelve a la pila. Tiene fines de comprobación */
	public Symbol nextAndUndo() {
		Symbol s = next();
		undo();
		return s;
	}
	
	/** Devuelve la línea que se está analizando en un momento determinado */
	public long getLineNumber() {
		return inputFile.getLineNumber();
	}
	
	
	/** Manda a reenviar el último símbolo emitido */
	public void undo() {
		resendSymbol = true;
	}
	
	
	/** Elimina los blancos al final de las lineas */
	public String trimEnd(String c) {
		while(c.endsWith(" ")) {
			c = c.substring(0, c.length()-1);
		}
		return c;
	}
	
}
