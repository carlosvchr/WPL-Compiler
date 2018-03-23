package mainpackage;

import java.io.IOException;

public class CodeGenerator {

	IOManager output;
	String path;
	
	/** Proceso: startHead() -> genMetadata()* -> genImports()* -> startBody() -> generate()* -> end() */
	
	public void startHead(String path) {
		this.path = path;
		output.openForWrite(path);
		output.putLine("<!Doctype html><html><head>");
	}
	
	/** Genera todos los metadatos de la página */
	public void genMetadata(String[] meta) {
		switch(meta[0]) {
		case "charset":
			output.putLine("<meta charset="+meta[1]+">");
			break;
		case "lang":
			output.putLine("<meta lang="+meta[1]+">");
			break;
		case "redirect":
			output.putLine("<meta http-equiv=\"refresh\" content=\""+meta[1]+"\">");
			break;
		case "author":
			output.putLine("<meta name=\"author\" content="+meta[1]+">");
			break;
		case "description":
			output.putLine("<meta name=\"description\" content="+meta[1]+">");
			break;
		case "keywords":
			output.putLine("<meta name=\"keywords\" content="+meta[1]+">");
			break;
		case "title":
			output.putLine("<title>"+meta[1]+"</title>");
			break;
		case "tabicon":
			output.putLine("<link rel=\"icon\" href=\""+meta[1]+"\">");
			break;		
		}
		
	}
	
	/** Genera el código necesario para incluir un fichero css o javascript */
	public void genImports(String path) {
		if(path.endsWith(".css")) {
			output.putLine("<link rel=\"stylesheet\" href=\""+path+"\">");
		}else if(path.endsWith(".js")) {
			output.putLine("<script src=\""+path+"\"></script>");
		}
	}
	
	/** Termina de generar código en el head y empieza a generarlo en el body */
	public void startBody() {
		output.putLine("</head><body>");
	}
	
	
	/** Aquí se generan todas las sentencias del body */
	public void generate() {
		
	}
	
	/** Termina de escribir el fichero */
	public void end() {
		output.putLine("</body></html>");
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
