package mainpackage;

import java.io.IOException;

public class CodeGenerator {

	IOManager output;
	String path;
	
	/** Proceso: startHead() -> genMetadata()* -> genImports()* -> startBody() -> generate()* -> end() */
	
	public CodeGenerator(String path) {
		this.path = path;
		output = new IOManager();
	}
	
	public void startHead() {
		output.openForWrite(path);
		output.putLine("<!Doctype html>\n<html>\n<head>");
	}
	
	/** Genera todos los metadatos de la página */
	public void genMetadata(String[] meta) {
		switch(meta[0]) {
		case Lexer._charset:
			// Le quitamos el prefijo cs- y le añadimos comillas
			meta[1] = "\"" + meta[1].replaceAll("cs-", "") + "\"";
			output.putLine("<meta charset="+meta[1]+">");
			break;
		case Lexer._lang:
			output.putLine("<meta lang="+meta[1]+">");
			break;
		case Lexer._redirect:
			output.putLine("<meta http-equiv=\"refresh\" content=\""+meta[1]+"\">");
			break;
		case Lexer._author:
			output.putLine("<meta name=\"author\" content="+meta[1]+">");
			break;
		case Lexer._description:
			output.putLine("<meta name=\"description\" content="+meta[1]+">");
			break;
		case Lexer._keywords:
			output.putLine("<meta name=\"keywords\" content="+meta[1]+">");
			break;
		case Lexer._title:
			// Le quitamos las comillas
			meta[1] = meta[1].substring(1, meta[1].length()-1);
			output.putLine("<title>"+meta[1]+"</title>");
			break;
		case Lexer._pageicon:
			output.putLine("<link rel=\"icon\" href=\""+meta[1]+"\">");
			break;		
		}
		
	}
	
	/** Genera el código necesario para incluir un fichero css o javascript */
	public void genImport(String path) {
		if(path.endsWith(".css\"")) {
			output.putLine("<link rel=\"stylesheet\" href="+path+">");
		}else if(path.endsWith(".js\"")) {
			output.putLine("<script src="+path+"></script>");
		}
	}
	
	/** Termina de generar código en el head y empieza a generarlo en el body */
	public void startBody() {
		output.putLine("</head>\n<body>");
	}
	
	
	/** Aquí se generan todas las sentencias del body */
	public void generate() {
		
	}
	
	/** Termina de escribir el fichero */
	public void end() {
		output.putLine("</body>\n</html>");
		try {
			output.close();
		} catch (IOException e) {
			System.err.println("Code Generator: Error when closing the file.");
			e.printStackTrace();
		}
	}
	
	/** Aborta la generación de código cuando se ha producido un error */
	public void abort() {
		try {
			output.close();
		} catch (IOException e) {
			System.err.println("Code Generator: Error when closing the file.");
			e.printStackTrace();
		}
		output.deleteFile(path);
	}
	
	
}
