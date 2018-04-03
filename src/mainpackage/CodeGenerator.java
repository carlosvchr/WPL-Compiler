package mainpackage;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class CodeGenerator {

	IOManager output;
	String path;
	int openedTags;
	int radiogroups = 0;
	
	Deque<Symbol> containerStack;
	
	// Este atributo almacena la linea del tag hasta que se complete, entonces se emite
	String currentLine;
	
	/** Proceso: startHead() -> genMetadata()* -> genImports()* -> startBody() -> generate()* -> end() */
	
	public CodeGenerator(String path, ArrayDeque<Symbol> ad) {
		this.path = path;
		this.containerStack = ad;
		output = new IOManager();
		openedTags = 0;
		currentLine = "";
	}
	
	public void startHead() {
		output.openForWrite(path);
		output.putLine("<!Doctype html>\n<html>\n<head>");
		output.putLine("\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		output.putLine("\t<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">");
		openedTags = 1;
	}
	
	/** Genera todos los metadatos de la página */
	public void genMetadata(String[] meta) {
		switch(meta[0]) {
		case Lexer._charset:
			// Le quitamos el prefijo cs- y le añadimos comillas
			meta[1] = "\"" + meta[1].replaceAll("cs-", "") + "\"";
			output.putLine(tabs()+"<meta charset="+meta[1]+">");
			break;
		case Lexer._lang:
			output.putLine(tabs()+"<meta lang="+meta[1]+">");
			break;
		case Lexer._redirect:
			output.putLine(tabs()+"<meta http-equiv=\"refresh\" content=\""+meta[1]+"\">");
			break;
		case Lexer._author:
			output.putLine(tabs()+"<meta name=\"author\" content="+meta[1]+">");
			break;
		case Lexer._description:
			output.putLine(tabs()+"<meta name=\"description\" content="+meta[1]+">");
			break;
		case Lexer._keywords:
			output.putLine(tabs()+"<meta name=\"keywords\" content="+meta[1]+">");
			break;
		case Lexer._title:
			// Le quitamos las comillas
			meta[1] = meta[1].substring(1, meta[1].length()-1);
			output.putLine(tabs()+"<title>"+meta[1]+"</title>");
			break;
		case Lexer._pageicon:
			output.putLine(tabs()+"<link rel=\"icon\" href=\""+meta[1]+"\">");
			break;		
		}
		
	}
	
	/** Genera el código necesario para incluir un fichero css o javascript */
	public void genImport(String path) {
		if(path.endsWith(".css\"")) {
			output.putLine(tabs()+"<link rel=\"stylesheet\" href="+path+">");
		}else if(path.endsWith(".js\"")) {
			output.putLine(tabs()+"<script src="+path+"></script>");
		}
	}
	
	/** Termina de generar código en el head y empieza a generarlo en el body */
	public void startBody() {
		output.putLine("</head>\n<body>");
		// Aqui antes de head tabs = 1, entre head y body = 0 y despues de body 1, por lo que lo dejamos igual
		// y simplemente no emitimos tabs entre head y body
	}
	
	
	/** Aquí se generan todas las sentencias del body */
	public void openTag(String tag) {
		String inheritedAttrs = "";
		
		// Si este nuevo elemento se crea dentro de un hbox...
		if(containerStack.peekLast().val().compareTo(Lexer._hbox) == 0) {
			inheritedAttrs += " w3-cell";
		}
		
		switch(tag) {
		case Lexer._box:
			currentLine = tabs()+"<div class=\"w3-display-container\"";
			break;
		case Lexer._hbox:
			currentLine = tabs()+"<div";
			break;
		case Lexer._vbox:
			currentLine = tabs()+"<div";
			break;
		case Lexer._sidebox:
			currentLine = tabs()+"<div class=\"w3-sidebar w3-bar-block\"";
			break;
		case Lexer._modalbox:
			currentLine = tabs()+"<div class=\"w3-modal\"><div class=\"w3-modal-container\"";
			break;
		case Lexer._tablebox:
			currentLine = tabs()+"<table ";
			break;
		case Lexer._dropdownbox:
			currentLine = tabs()+"<div class=\"w3-dropdown-click\"";
			break;
		case Lexer._tabbedbox:
			currentLine = tabs()+"<div";
			break;
		case Lexer._accordionbox:
			currentLine = tabs()+"<div";
			break;
		case Lexer._slideshow:
			currentLine = tabs()+"<div";
			break;
		case Lexer._radiogroup:
			currentLine = tabs()+"<div";
			radiogroups++;
			break;
		case Lexer._radiobutton:
			currentLine = tabs()+"<input class=\"w3-radio\" type=\"radio\" name=\"rg-"+radiogroups+"\"";
			break;
		case Lexer._button:
			currentLine = tabs()+"<button class=\"w3-button\"";
			break;
		case Lexer._image:
			currentLine = tabs()+"<img";
			break;
		case Lexer._video:
			currentLine = tabs()+"<video";
			break;
		case Lexer._audio:
			currentLine = tabs()+"<audio";
			break;
		case Lexer._textfield:
			currentLine = tabs()+"<input class=\"w3-input\" type=\"text\" ";
			break;
		case Lexer._checkbox:
			currentLine = tabs()+"<input class=\"w3-check\" type=\"checkbox\" ";
			break;
		case Lexer._label:
			currentLine = tabs()+"<div";
			break;
		case Lexer._progressbar:
			currentLine = tabs()+"<div";
			break;
		case Lexer._item:
			currentLine = tabs()+"<div";
			break;
		}
	}
	
	/** Escribe los atributos de las etiquetas */
	public void genAttrs(String attr, String[] values) {
		for(int i=0; i<values.length; i++) {
			values[i] = clean(values[i]);
		}
		switch(attr) {
		case Lexer._alt:
			currentLine += " alt="+values[0];
			break;
		case Lexer._poster:
			currentLine += " poster="+values[0];
			break;
		case Lexer._src:
			currentLine += " src="+values[0];
			break;
		case Lexer._autoplay:
			if(values[0].compareTo(Lexer._true) == 0) currentLine += " autoplay";
			break;
		case Lexer._controls:
			if(values[0].compareTo(Lexer._true) == 0) currentLine += " controls";
			break;
		case Lexer._loop:
			if(values[0].compareTo(Lexer._true) == 0) currentLine += " loop";
			break;
		case Lexer._muted:
			if(values[0].compareTo(Lexer._true) == 0) currentLine += " muted";
			break;
		case Lexer._preload:
			if(values[0].compareTo(Lexer._false) == 0) currentLine += " preload=\"none\"";
			break;
		case Lexer._onclick:
			currentLine += " onclick="+values[0];
			break;
		case Lexer._onchange:
			currentLine += " onchange="+values[0];
			break;
		case Lexer._align:
		case Lexer._type:
		case Lexer._placeholder:
		case Lexer._header:
		case Lexer._text_align:
			addStyle("text-align:"+values[0]);
			break;
		case Lexer._text_decoration:
		case Lexer._text_color:
			addStyle("color:"+values[0]);
			break;
		case Lexer._text:
		case Lexer._font_size:
		case Lexer._font_family:
		case Lexer._animation:
		case Lexer._bgcolor:
			addStyle("background-color:"+values[0]);
			break;
		case Lexer._border_color:
			addStyle("border-color:"+values[0]);
			break;
		case Lexer._border_radius:
			addStyle("border-radius:"+getJoined(values));
			break;
		case Lexer._border:
			addStyle("border-width:"+getJoined(values));
			break;
		case Lexer._class:
			addClass(values[0]);
			break;
		case Lexer._effect:
		case Lexer._elevation:
		case Lexer._height:
			addStyle("height:"+values[0]);
			break;
		case Lexer._id:
			currentLine += " id="+values[0];
			break;
		case Lexer._link:
		case Lexer._margin:
			addStyle("margin:"+getJoined(values));
			break;
		case Lexer._padding:
			addStyle("padding:"+getJoined(values));
			break;
		case Lexer._slots:
		case Lexer._tooltip:
			currentLine += " title="+values[0];
			break;
		case Lexer._width:
			addStyle("width:"+values[0]);
			break;
		case Lexer._closable:
		case Lexer._delay:
		case Lexer._slide_controls:
		case Lexer._indicators:
		case Lexer._caption_position:
		case Lexer._caption:
		case Lexer._selected:
		}
	}
	
	/** Termina de escribir la etiqueta de apertura para proceder al contenido */
	public void finishOpenTag() {
		output.putString(currentLine+">\n");
		openedTags ++;
	}
	
	/** Escribe contenido entre las etiquetas */
	public void genContent(String s) {
		output.putString(s);
	}
	
	/** Cierra la etiqueta abierta */
	public void closeTag(String tag) {
		openedTags--;
		switch(tag) {
		case Lexer._box:
			output.putLine(tabs()+"</div>");
			break;
		case Lexer._hbox:
			output.putLine(tabs()+"</div>");
			break;
		case Lexer._vbox:
			output.putLine(tabs()+"</div>");
			break;
		case Lexer._sidebox:
			output.putLine(tabs()+"</div>");
			break;
		case Lexer._modalbox:
			output.putLine(tabs()+"</div>");
			break;
		case Lexer._tablebox:
			output.putLine(tabs()+"</table>");
			break;
		case Lexer._dropdownbox:
			output.putLine(tabs()+"</div>");
			break;
		case Lexer._tabbedbox:
			output.putLine(tabs()+"</div>");
			break;
		case Lexer._accordionbox:
			output.putLine(tabs()+"</div>");
			break;
		case Lexer._slideshow:
			output.putLine(tabs()+"</div>");
			break;
		case Lexer._radiogroup:
			output.putLine(tabs()+"</div>");
			break;
		case Lexer._radiobutton:
			output.putLine(tabs()+"</input>");
			break;
		case Lexer._button:
			output.putLine(tabs()+"</button>");
			break;
		case Lexer._image:
			output.putLine(tabs()+"</img>");
			break;
		case Lexer._video:
			output.putLine(tabs()+"</video>");
			break;
		case Lexer._audio:
			output.putLine(tabs()+"</audio>");
			break;
		case Lexer._textfield:
			output.putLine(tabs()+"</input>");
			break;
		case Lexer._checkbox:
			output.putLine(tabs()+"</input>");
			break;
		case Lexer._label:
			output.putLine(tabs()+"</div>");
			break;
		case Lexer._progressbar:
			output.putLine(tabs()+"</div>");
			break;
		case Lexer._item:
			output.putLine(tabs()+"</div>");
			break;
		}
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
	
	/** Devuelve el numero de tabuladores de una linea */
	public String tabs() {
		String s = "";
		for(int i=0; i<openedTags; i++) {
			s += "    ";
		}
		return s;
	}
	
	/** Introduce un atributo dentro del atributo class */
	public void addClass(String attr) {
		// Comprobamos que se haya declarado el atributo class y, si no, lo creamos
		if(!currentLine.contains("class=\"")) {
			currentLine = currentLine + " class=\"" + attr + "\"";
		}else {	
			String[] sp = currentLine.split("class=\"");
			if(sp.length == 2) {
				currentLine = sp[0] + "class=\"" + attr + " " + sp[1];
			}
		}
		
	}
	
	/** Introduce un atributo dentro del atributo style */
	public void addStyle(String attr) {
		// Comprobamos que se haya declarado el atributo style y, si no, lo creamos
		if(!currentLine.contains("style=\"")) {
			currentLine = currentLine + " style=\"" + attr + ";\"";
		}else {	
			String[] sp = currentLine.split("style=\"");
			if(sp.length == 2) {
				currentLine = sp[0] + "style=\"" + attr + "; " + sp[1];
			}
		}
	}
	
	
	/** Agrega una hiperenlace a la etiqueta */
	public void addLink(String url) {
		currentLine = "<a href=\""+url+"\">" + currentLine;
		// registrar que la etiqueta tiene hiper enlace para cerrarla luego
	}
	
	
	/** Devuelve varios valores unidos en una cadena */
	private String getJoined(String[] v) {
		String r = "";
		int i;
		for(i=0; i<v.length-1; i++) {
			r += v[i] + " ";
		}
		r += v[i];
		return r;
	}
	
	/** Elimina las comillas dobles */
	private String clean(String s) {
		if(s.startsWith("\"")) {
			s = s.substring(1, s.length());
		}
		if(s.endsWith("\"")) {
			s = s.substring(0, s.length()-1);
		}
		return s;
	}
	
	
}
