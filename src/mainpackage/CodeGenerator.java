package mainpackage;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class CodeGenerator {

	IOManager output;
	String path;
	int dropdownCounter;
	int accordionCounter;
	
	Deque<String[]> containerStack;
	
	// Este atributo almacena la linea del tag hasta que se complete, entonces se emite
	String currentLine;
	
	// Este atributo almacena el contenido que irá entre las etiquetas de la linea actual (del currentLine)
	String currentContent;
	
	/** Proceso: startHead() -> genMetadata()* -> genImports()* -> startBody() -> generate()* -> end() */
	
	public CodeGenerator(String path) {
		this.path = path;
		this.containerStack = new ArrayDeque<String[]>();
		output = new IOManager();
		currentLine = "";
		currentContent = "";
		dropdownCounter = 0;
	}
	
	public void startHead() {
		output.openForWrite(path);
		output.putLine("<!Doctype html>\n<html>\n<head>");
		output.putLine("\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		output.putLine("\t<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">");
		output.putLine("\t<script>\n");
		genPredefinedFunctions();
		output.putLine("\t</script>");
	}
	
	/** Genera todos los metadatos de la página */
	public void genMetadata(String[] meta) {
		switch(meta[0]) {
		case Lexer._charset:
			output.putLine(tabs(1)+"<meta charset="+meta[1]+">");
			break;
		case Lexer._lang:
			output.putLine(tabs(1)+"<meta lang="+meta[1]+">");
			break;
		case Lexer._redirect:
			output.putLine(tabs(1)+"<meta http-equiv=\"refresh\" content=\""+meta[1]+"\">");
			break;
		case Lexer._author:
			output.putLine(tabs(1)+"<meta name=\"author\" content="+meta[1]+">");
			break;
		case Lexer._description:
			output.putLine(tabs(1)+"<meta name=\"description\" content="+meta[1]+">");
			break;
		case Lexer._keywords:
			output.putLine(tabs(1)+"<meta name=\"keywords\" content="+meta[1]+">");
			break;
		case Lexer._title:
			output.putLine(tabs(1)+"<title>"+clean(meta[1])+"</title>");
			break;
		case Lexer._pageicon:
			output.putLine(tabs(1)+"<link rel=\"icon\" href=\""+meta[1]+"\">");
			break;		
		}
		
	}
	
	/** Genera el código necesario para incluir un fichero css o javascript */
	public void genImport(String path) {
		if(path.endsWith(".css\"")) {
			output.putLine(tabs(1)+"<link rel=\"stylesheet\" href="+path+">");
		}else if(path.endsWith(".js\"")) {
			output.putLine(tabs(1)+"<script src="+path+"></script>");
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
		String parent[] = containerStack.peekFirst();
		String s[] = new String[2];
		switch(tag) {
		case Lexer._accordion:
			accordionCounter++;
			currentLine = "<button style=\"\" onclick=\"toggleDropdown('accordion"+accordionCounter+"')\" class=\"w3-button w3-block\"></button>"+
							"<div id=\"accordion"+accordionCounter+"\" class=\"w3-hide\">";
			s[0] = Lexer._accordion;
			s[1] = "</div>";
			containerStack.push(s);
			break;
		case Lexer._dropdown:
			dropdownCounter++;
			currentLine = "<div class=\"w3-dropdown-hover\" style=\"\"><button style=\"width:100%; height:100%;\" class=\"w3-button\"></button>"+
							"<div id=\"dropdown"+dropdownCounter+"\""+
							" class=\"w3-dropdown-content w3-bar-block w3-border\">";
			s[0] = Lexer._dropdown;
			s[1] = "</div></div>";
			containerStack.push(s);
			break;
		case Lexer._hbox:
			currentLine = "<div>";
			s[0] = Lexer._hbox;
			s[1] = "</div>";
			containerStack.push(s);
			break;
		case Lexer._modal:
			currentLine = "<div class=\"w3-modal\"><div class=\"w3-modal-content\"><div class=\"w3-container\">";
			s[0] = Lexer._modal;
			s[1] = "</div></div></div>";
			containerStack.push(s);
			break;
		case Lexer._sidebar:
			currentLine = "<div class=\"w3-sidebar w3-bar-block\">";
			s[0] = Lexer._sidebar;
			s[1] = "</div>";
			containerStack.push(s);
			break;
		case Lexer._table:
			currentLine = "<table>";
			s[0] = Lexer._table;
			s[1] = "</table>";
			containerStack.push(s);
			break;
		case Lexer._hrow:
			currentLine = "<tr>";
			s[0] = Lexer._hrow;
			s[1] = "</tr>";
			containerStack.push(s);
			break;
		case Lexer._row:
			currentLine = "<tr>";
			s[0] = Lexer._row;
			s[1] = "</tr>";
			containerStack.push(s);
			break;
		case Lexer._vbox:
			currentLine = "<div>";
			s[0] = Lexer._vbox;
			s[1] = "</div>";
			containerStack.push(s);
			break;
		case Lexer._audio:
			currentLine = "<audio>";
			s[0] = Lexer._audio;
			s[1] = "</audio>";
			containerStack.push(s);
			break;
		case Lexer._button:
			currentLine = "<button class=\"w3-button\">";
			s[0] = Lexer._button;
			s[1] = "</button>";
			containerStack.push(s);
			break;
		case Lexer._checkbox:
			currentLine = tabs()+"<input class=\"w3-check\" type=\"checkbox\">";
			s[0] = Lexer._checkbox;
			s[1] = "</input>";
			containerStack.push(s);
			break;
		case Lexer._image:
			currentLine = "<img>";
			s[0] = Lexer._image;
			s[1] = "</img>";
			containerStack.push(s);
			break;
		case Lexer._label:
			currentLine = "<label>";
			s[0] = Lexer._label;
			s[1] = "</label>";
			containerStack.push(s);
			break;	
		case Lexer._radiobutton:
			currentLine = "<input class=\"w3-radio\" type=\"radio\">";
			s[0] = Lexer._radiobutton;
			s[1] = "</input>";
			containerStack.push(s);
			break;
		case Lexer._textfield:
			currentLine = "<input class=\"w3-input\" type=\"text\">";
			s[0] = Lexer._textfield;
			s[1] = "</input>";
			containerStack.push(s);
			break;
		case Lexer._video:
			currentLine = "<video>";
			s[0] = Lexer._video;
			s[1] = "</video>";
			containerStack.push(s);
			break;
		}
		
		/* Modificaciones por la herencia */
		if(parent != null) {
			if(parent[0].compareTo(Lexer._hrow)==0) {
				currentLine = "<th>"+currentLine;
				containerStack.peekFirst()[1] += "</th>";
			}else if(parent[0].compareTo(Lexer._row)==0) {
				currentLine = "<td>"+currentLine;
				containerStack.peekFirst()[1] += "</td>";
			}else if(parent[0].compareTo(Lexer._hbox)==0) {
				addClass("w3-mobile");
				addStyle("display:inline-block");
			}else if(parent[0].compareTo(Lexer._vbox)==0) {
				containerStack.peekFirst()[1] += "<div style=\"width:100%;\"></div>";
			}else if(parent[0].compareTo(Lexer._dropdown)==0) {
				addClass("w3-bar-item");
			}
		}
		
	}
	
	/** Escribe los atributos de las etiquetas */
	public void genAttrs(String attrs, String val) {
		String values[] = {val};
		genAttrs(attrs, values);
	}
	
	/** Escribe los atributos de las etiquetas */
	public void genAttrs(String attr, Symbol[] values) {
		String strl[] = new String[values.length];
		for(int i=0; i<values.length; i++) {
			strl[i] = values[i].val();
		}
		genAttrs(attr, strl);
	}
	
	/** Escribe los atributos de las etiquetas */
	public void genAttrs(String attr, String[] values) {
		for(int i=0; i<values.length; i++) {
			values[i] = clean(values[i]);
		}
		switch(attr) {
		case Lexer._align:
			switch(values[0]) {
			case Lexer._top: addClass("w3-display-topmiddle"); break;
			case Lexer._bottom: addClass("w3-display-bottommiddle"); break;
			case Lexer._right: addClass("w3-display-right"); break;
			case Lexer._left: addClass("w3-display-left"); break;
			case Lexer._topleft: addClass("w3-display-topleft"); break;
			case Lexer._topright: addClass("w3-display-topright"); break;
			case Lexer._bottomleft: addClass("w3-display-bottomleft"); break;
			case Lexer._bottomright: addClass("w3-display-bottomright"); break;
			case Lexer._center: addClass("w3-display-middle"); break;
			}
			break;
		case Lexer._alt:
			addAttr("alt=\""+values[0]+"\"");
			break;
		case Lexer._animation:
			switch(values[0]) {
			case Lexer._zoom: addClass("w3-animate-zoom"); break;
			case Lexer._fading: addClass("w3-animate-fading"); break;
			case Lexer._fadein: addClass("w3-animate-opacity"); break;
			case Lexer._spin: addClass("w3-spin"); break;
			case Lexer._moveup: addClass("w3-animate-top"); break;
			case Lexer._movedown: addClass("w3-animate-bottom"); break;
			case Lexer._moveright: addClass("w3-animate-right"); break;
			case Lexer._moveleft: addClass("w3-animate-left"); break;
			}
			break;
		case Lexer._autoplay:
			if(values[0].compareTo(Lexer._true) == 0) addAttr("autoplay");
			break;
		case Lexer._bgcolor:
			addStyle("background-color:"+values[0]);
			break;	
		case Lexer._border:
			addStyle("border-style:solid; border-width:"+getJoined(values));
			break;
		case Lexer._bordercolor:
			addStyle("border-color:"+values[0]);
			break;
		case Lexer._borderradius:
			addStyle("border-radius:"+getJoined(values));
			break;
		case Lexer._class:
			addClass(values[0]);
			break;
		case Lexer._collapsible:
			break;
		case Lexer._controls:
			if(values[0].compareTo(Lexer._true) == 0) addAttr("controls");
			break;
		case Lexer._dropdowntype:
			if(values[0].compareTo(Lexer._clickable)==0) {			
				currentLine = currentLine.replace("w3-dropdown-hover", "w3-dropdown-click");
				String aux[] = currentLine.split("<button");
				currentLine = aux[0] + "<button onclick=\"toggleDropdown('dropdown"+dropdownCounter+"')\""+aux[1];
				currentLine = currentLine.substring(0, currentLine.length()-1) + " onclick=\"toggleDropdown('dropdown"+dropdownCounter+"')\">";			
			}
			break;
		case Lexer._effect:
			switch(values[0]) {
			case Lexer._opacity: addClass("w3-opacity"); break;
			case Lexer._opacitymin: addClass("w3-opacity-min"); break;
			case Lexer._opacitymax: addClass("w3-opacity-max"); break;
			case Lexer._sepia: addClass("w3-sepia"); break;
			case Lexer._sepiamin: addClass("w3-sepia-min"); break;
			case Lexer._sepiamax: addClass("w3-sepia-max"); break;
			case Lexer._grayscale: addClass("w3-grayscale"); break;
			case Lexer._grayscalemin: addClass("w3-grayscale-min"); break;
			case Lexer._grayscalemax: addClass("w3-grayscale-max"); break;
			}
			break;
		case Lexer._elevation:
			int level = Integer.parseInt(values[0]);
			addStyle("box-shadow:0 "+2*level+"px "+5*level+"px 0 rgba(0,0,0,0.16),0 "+2*level+"px "+10*level+"px 0 rgba(0,0,0,0.12)");
			break;
		case Lexer._fontfamily:
			addStyle("font-family:'"+values[0]+"'");
			break;
		case Lexer._fontsize:
			addStyle("font-size:"+values[0]);
			break;
		case Lexer._height:
			addStyle("height:"+values[0]);
			break;
		case Lexer._id:
			addAttr("id=\""+values[0]+"\"");
			break;
		case Lexer._link:
			currentLine = "<a href=\""+values[0]+"\">" + currentLine;
			String[] s = {containerStack.peekLast()[0], containerStack.pop()[1]+"</a>"};
			containerStack.push(s);
			break;
		case Lexer._loop:
			if(values[0].compareTo(Lexer._true) == 0) addAttr("loop");
			break;
		case Lexer._margin:
			addStyle("margin:"+getJoined(values));
			break;	
		case Lexer._muted:
			if(values[0].compareTo(Lexer._true) == 0) addAttr("muted");
			break;	
		case Lexer._onchange:
			addAttr("onchange=\""+values[0]+"\"");
			break;
		case Lexer._onclick:
			addAttr("onclick=\""+values[0]+"\"");
			break;	
		case Lexer._padding:
			addStyle("padding:"+getJoined(values));
			break;
		case Lexer._placeholder:
			addAttr("placeholder=\""+values[0]+"\"");
			break;		
		case Lexer._poster:
			addAttr("poster="+values[0]);
			break;
		case Lexer._preload:
			if(values[0].compareTo(Lexer._false) == 0) addAttr("preload=\"none\"");
			else addAttr("preload=\"auto\"");
			break;
		case Lexer._radiogroup:
			addAttr("name=\""+values[0]+"\"");
			break;
		case Lexer._selected:
			addAttr("checked=\"checked\"");
			break;
		case Lexer._sidebartype:
			break;		
		case Lexer._src:
			addAttr("src=\""+values[0]+"\"");
			break;	
		case Lexer._spacing:
			break;
		case Lexer.__text:
			String aux = containerStack.peekFirst()[0];
			if(aux.compareTo(Lexer._dropdown)==0 || aux.compareTo(Lexer._accordion)==0) {
				String lineaux[] = currentLine.split("</button>");
				currentLine = lineaux[0] + values[0] + "</button>" + lineaux[1];
			}else {
				currentContent = values[0];
			}
			break;
		case Lexer._textalign:
			addStyle("text-align:"+values[0]);
			break;
		case Lexer._textcolor:
			addStyle("color:"+values[0]);
			break;
		case Lexer._textdecoration:
			String textDecoration = "";
			for(int i=0; i<values.length; i++) {
				switch(values[i]) {
				case Lexer._italic: addStyle("font-style:italic"); break;
				case Lexer._bold: addStyle("font-weight:bold"); break;
				case Lexer._overline: textDecoration += " overline"; break;
				case Lexer._underline: textDecoration += " underline"; break;
				case Lexer._strikethrough: textDecoration += " line-through"; break;
				}
			}
			if(textDecoration.length()>0) {
				addStyle("text-decoration:"+textDecoration);
			}
			break;
		case Lexer._tooltip:
			addAttr("title=\""+values[0]+"\"");
			break;
		case Lexer._width:	
			addStyle("width:"+values[0]);
			break;
			default: break;
		}
	}
	
	/** Termina de escribir la etiqueta de apertura para proceder al contenido */
	public void finishOpenTag() {
		output.putString(tabs()+currentLine+"\n");
		if(currentContent.length() > 0) {
			output.putLine(tabs(1)+currentContent);
			currentContent = "";
		}
	}
	
	/** Escribe contenido entre las etiquetas */
	public void genContent(String s) {
		output.putString(s);
	}
	
	/** Cierra la etiqueta abierta */
	public void closeTag() {
		output.putLine(tabs()+containerStack.pop()[1]);
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
	private String tabs() {
		String s = "";
		for(int i=0; i<containerStack.size(); i++) {
			s += "    ";
		}
		return s;
	}
	
	
	/** Devuelve n numero de tabs mas del actual de la linea */
	private String tabs(int n) {
		String s = "";
		for(int i=0; i<containerStack.size() + n; i++) {
			s += "    ";
		}
		return s;
	}
	
	/** Introduce un atributo dentro del atributo class */
	private void addClass(String attr) {
		// Comprobamos que se haya declarado el atributo class y, si no, lo creamos
		if(!currentLine.contains("class=\"")) {
			currentLine = currentLine.substring(0, currentLine.length()-1) + " class=\"" + attr + "\">";
		}else {	
			String[] sp = currentLine.split("class=\"");
			if(sp.length == 2) {
				currentLine = sp[0] + "class=\"" + attr + " " + sp[1];
			}
		}
	}
	
	/** Introduce un atributo dentro del atributo style */
	private void addStyle(String attr) {
		// Comprobamos que se haya declarado el atributo style y, si no, lo creamos
		if(!currentLine.contains("style=\"")) {
			currentLine = currentLine.substring(0, currentLine.length()-1) + " style=\"" + attr + ";\">";
		}else {	
			String aux = containerStack.peekFirst()[0];
			String[] sp = currentLine.split("style=\"");
			if(sp.length == 2) {
				currentLine = sp[0] + "style=\"" + attr + "; " + sp[1];
			}
			if(aux.compareTo(Lexer._dropdown)==0) {
				currentLine = sp[0] + "style=\"" + attr + "; " + sp[1] + "style=\"" + sp[2];
			}
		}
	}
	
	
	/** Agrega un atributo a la etiqueta actual */
	private void addAttr(String attr) {
		if(containerStack.peekFirst()[0].compareTo(Lexer._modal)==0) {
			if(attr.startsWith("id")) {
				String parts[] = currentLine.split("><");
				currentLine = parts[0] + " " + attr;
				for(int i=1; i<parts.length; i++) {
					currentLine += "><" + parts[i];
				}
			}else {
				currentLine = currentLine.substring(0, currentLine.length()-1)+" "+attr+">";
			}
			currentContent = "<span onclick=\"closeModal('"+clean(attr.split("=")[1])+"')\" "+
							"style=\"z-index:999;\" class=\"w3-button w3-display-topright\">&times;</span>";
		}else{
			currentLine = currentLine.substring(0, currentLine.length()-1)+" "+attr+">";
		}
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
	
	
	/** Genera las funciones predefinidas para el correcto funcionamiento de algunos componentes */
	private void genPredefinedFunctions() {
		output.putLine(
				tabs(2)+"function openModal(identifier) {\n" + 
				tabs(3)+"document.getElementById(identifier).style.display='block';\n" + 
				tabs(2)+"}\n");
		output.putLine(
				tabs(2)+"function closeModal(identifier) {\n" + 
				tabs(3)+"document.getElementById(identifier).style.display='none';\n" + 
				tabs(2)+"}\n");
		output.putLine(
				tabs(2)+"function toggleDropdown(identifier) {\n" + 
				tabs(3)+"var x = document.getElementById(identifier);\n" + 
				tabs(3)+"if (x.className.indexOf(\"w3-show\") == -1) {\n" + 
				tabs(4)+"x.className += \" w3-show\";\n" + 
				tabs(3)+"} else { \n" + 
				tabs(4)+"x.className = x.className.replace(\" w3-show\", \"\");\n"+
				tabs(3)+"}\n" + 
				tabs(2)+"}\n");
	}
	
}
