package mainpackage;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class CodeGenerator {

	IOManager output;
	String path;
	int radiogroups = 0;
	int tabbedboxs = 0;
	int itemcont = 0;
	
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
	}
	
	public void startHead() {
		output.openForWrite(path);
		output.putLine("<!Doctype html>\n<html>\n<head>");
		output.putLine("\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		output.putLine("\t<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">");
	}
	
	/** Genera todos los metadatos de la página */
	public void genMetadata(String[] meta) {
		switch(meta[0]) {
		case Lexer._charset:
			// Le quitamos el prefijo cs- y le añadimos comillas
			meta[1] = "\"" + meta[1].replaceAll("cs-", "") + "\"";
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
		
		String s[] = new String[2];
		switch(tag) {
		case Lexer._box:
			currentLine = "<div class=\"w3-display-container\">";
			s[0] = Lexer._box;
			s[1] = "</div>";
			containerStack.push(s);
			break;
		case Lexer._hbox:
			currentLine = "<div>";
			s[0] = Lexer._hbox;
			s[1] = "</div>";
			containerStack.push(s);
			break;
		case Lexer._vbox:
			currentLine = "<div>";
			s[0] = Lexer._vbox;
			s[1] = "</div>";
			containerStack.push(s);
			break;
		case Lexer._sidebox:
			currentLine = "<div class=\"w3-sidebar w3-bar-block\">";
			s[0] = Lexer._sidebox;
			s[1] = "</div>";
			containerStack.push(s);
			break;
		case Lexer._modalbox:
			currentLine = "<div class=\"w3-modal\"><div class=\"w3-modal-container\">";
			s[0] = Lexer._modalbox;
			s[1] = "</div>";
			containerStack.push(s);
			break;
		case Lexer._tablebox:
			currentLine = "<table>";
			s[0] = Lexer._tablebox;
			s[1] = "</table>";
			containerStack.push(s);
			break;
		case Lexer._dropdownbox:
			currentLine = "<div class=\"w3-dropdown-click\">";
			s[0] = Lexer._dropdownbox;
			s[1] = "</div>";
			containerStack.push(s);
			break;
		case Lexer._tabbedbox:
			itemcont = 0;
			tabbedboxs ++;
			output.putLine(tabs(1)+"<script>\n" +
					tabs(2)+"function openTab(v, vclass) {\n" + 
					tabs(2)+"    var i;\n" + 
					tabs(2)+"    var x = document.getElementsByClassName(vclass);\n" + 
					tabs(2)+					"    for (i = 0; i < x.length; i++) {\n" + 
					tabs(2)+"        x[i].style.display = \"none\"; \n" + 
					tabs(2)+"    }\n" + 
					tabs(2)+"    document.getElementById(v).style.display = \"block\"; \n" + 
					tabs(2)+"}\n"+
					tabs(1)+"</script>");
			currentLine = "<div>";
			s[0] = Lexer._tabbedbox;
			s[1] = "</div>";
			containerStack.push(s);
			break;
		case Lexer._accordionbox:
			currentLine = "<div>";
			s[0] = Lexer._accordionbox;
			s[1] = "</div>";
			containerStack.push(s);
			break;
		case Lexer._slideshow:
			currentLine = "<div>";
			s[0] = Lexer._slideshow;
			s[1] = "</div>";
			containerStack.push(s);
			break;
		case Lexer._radiogroup:
			currentLine = "<div>";
			s[0] = Lexer._radiogroup;
			s[1] = "</div>";
			containerStack.push(s);
			radiogroups++;
			break;
		case Lexer._radiobutton:
			currentLine = "<input class=\"w3-radio\" type=\"radio\" name=\"rg-"+radiogroups+"\">";
			s[0] = Lexer._radiobutton;
			s[1] = "</input>";
			containerStack.push(s);
			break;
		case Lexer._button:
			currentLine = "<button class=\"w3-button\">";
			s[0] = Lexer._button;
			s[1] = "</button>";
			containerStack.push(s);
			break;
		case Lexer._image:
			currentLine = "<img>";
			s[0] = Lexer._image;
			s[1] = "</img>";
			containerStack.push(s);
			break;
		case Lexer._video:
			currentLine = "<video>";
			s[0] = Lexer._video;
			s[1] = "</video>";
			containerStack.push(s);
			break;
		case Lexer._audio:
			currentLine = "<audio>";
			s[0] = Lexer._audio;
			s[1] = "</audio>";
			containerStack.push(s);
			break;
		case Lexer._textfield:
			currentLine = "<input class=\"w3-input\" type=\"text\">";
			s[0] = Lexer._textfield;
			s[1] = "</input>";
			containerStack.push(s);
			break;
		case Lexer._checkbox:
			currentLine = tabs()+"<input class=\"w3-check\" type=\"checkbox\">";
			s[0] = Lexer._checkbox;
			s[1] = "</input>";
			containerStack.push(s);
			break;
		case Lexer._label:
			currentLine = "<div>";
			s[0] = Lexer._label;
			s[1] = "</div>";
			containerStack.push(s);
			break;
		case Lexer._progressbar:
			currentLine = "<div>";
			s[0] = Lexer._progressbar;
			s[1] = "</div>";
			containerStack.push(s);
			break;
		case Lexer._item:
			switch(getCurrent()) {
			case Lexer._tablebox:
				currentLine = "<tr>";
				s[0] = Lexer._item;
				s[1] = "</tr>";
				containerStack.push(s);
				break;
			case Lexer._dropdownbox:
			case Lexer._tabbedbox:
				itemcont++;
				currentLine = "<button onclick=\"openTab('tab"+itemcont+"', 'tabbedbox"+tabbedboxs+"')\">click</button>\n"+tabs(1);
				currentLine += "<div>";
				addClass("tabbedbox"+tabbedboxs);
				addAttr("id=\"tab"+itemcont+"\"");
				s[0] = Lexer._item;
				s[1] = "</div>";
				containerStack.push(s);
				break;
			case Lexer._slideshow:
			case Lexer._accordionbox:
			default:
				currentLine = "<div>";
				s[0] = Lexer._item;
				s[1] = "</div>";
				containerStack.push(s);
				break;
			}
			break;
		}
		
		// Comprobamos en qué contenedor estamos para heredar los atributos del contenedor padre
		switch(getParent()) {
		case Lexer._hbox:
			break;
		case Lexer._item:
			switch(getGrandParent()) {
			case Lexer._tablebox:
				currentLine = "<td>"+currentLine;
				String[] it = {containerStack.peekFirst()[0], containerStack.pop()[1]+"</td>"};
				containerStack.push(it);
			}		
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
			addAttr("alt="+values[0]);
			break;
		case Lexer._poster:
			addAttr("poster="+values[0]);
			break;
		case Lexer._src:
			addAttr("src="+values[0]);
			break;
		case Lexer._autoplay:
			if(values[0].compareTo(Lexer._true) == 0) addAttr("autoplay");
			break;
		case Lexer._controls:
			if(values[0].compareTo(Lexer._true) == 0) addAttr("controls");
			break;
		case Lexer._loop:
			if(values[0].compareTo(Lexer._true) == 0) addAttr("loop");
			break;
		case Lexer._muted:
			if(values[0].compareTo(Lexer._true) == 0) addAttr("muted");
			break;
		case Lexer._preload:
			if(values[0].compareTo(Lexer._false) == 0) addAttr("preload=\"none\"");
			break;
		case Lexer._onclick:
			addAttr("onclick=\""+values[0]+"\"");
			break;
		case Lexer._onchange:
			addAttr("onchange=\""+values[0]+"\"");
			break;
		case Lexer._align:
			switch(values[0]) {
			case Lexer._top: addClass("w3-display-topmiddle"); break;
			case Lexer._bottom: addClass("w3-display-bottommiddle"); break;
			case Lexer._right: addClass("w3-display-right"); break;
			case Lexer._left: addClass("w3-display-left"); break;
			case Lexer._top_left: addClass("w3-display-topleft"); break;
			case Lexer._top_right: addClass("w3-display-topright"); break;
			case Lexer._bottom_left: addClass("w3-display-bottomleft"); break;
			case Lexer._bottom_right: addClass("w3-display-bottomright"); break;
			case Lexer._center: addClass("w3-display-middle"); break;
			}
			break;
		case Lexer._type:
		case Lexer._placeholder:
			addAttr("placeholder=\""+values[0]+"\"");
			break;
		case Lexer._header:
		case Lexer._text_align:
			addStyle("text-align:"+values[0]);
			break;
		case Lexer._text_decoration:
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
		case Lexer._text_color:
			addStyle("color:"+values[0]);
			break;
		case Lexer._text:
			currentContent += values[0];
			break;
		case Lexer._font_size:
			addStyle("font-size:"+values[0]);
			break;
		case Lexer._font_family:
			addStyle("font-family:'"+values[0]+"'");
			break;
		case Lexer._animation:
			switch(values[0]) {
			case Lexer._zoom: addClass("w3-animate-zoom"); break;
			case Lexer._fading: addClass("w3-animate-fading"); break;
			case Lexer._fade_in: addClass("w3-animate-opacity"); break;
			case Lexer._spin: addClass("w3-spin"); break;
			case Lexer._move_up: addClass("w3-animate-top"); break;
			case Lexer._move_down: addClass("w3-animate-bottom"); break;
			case Lexer._move_right: addClass("w3-animate-right"); break;
			case Lexer._move_left: addClass("w3-animate-left"); break;
			}
			break;
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
			addStyle("border-style:solid; border-width:"+getJoined(values));
			break;
		case Lexer._class:
			addClass(values[0]);
			break;
		case Lexer._effect:
			switch(values[0]) {
			case Lexer._opacity: addClass("w3-opacity"); break;
			case Lexer._opacity_min: addClass("w3-opacity-min"); break;
			case Lexer._opacity_max: addClass("w3-opacity-max"); break;
			case Lexer._sepia: addClass("w3-sepia"); break;
			case Lexer._sepia_min: addClass("w3-sepia-min"); break;
			case Lexer._sepia_max: addClass("w3-sepia-max"); break;
			case Lexer._grayscale: addClass("w3-grayscale"); break;
			case Lexer._grayscale_min: addClass("w3-grayscale-min"); break;
			case Lexer._grayscale_max: addClass("w3-grayscale-max"); break;
			}
			break;
		case Lexer._elevation:
			int level = Integer.parseInt(values[0]);
			addStyle("box-shadow:0 "+2*level+"px "+5*level+"px 0 rgba(0,0,0,0.16),0 "+2*level+"px "+10*level+"px 0 rgba(0,0,0,0.12)");
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
		case Lexer._margin:
			addStyle("margin:"+getJoined(values));
			break;
		case Lexer._padding:
			addStyle("padding:"+getJoined(values));
			break;
		case Lexer._slots:
		case Lexer._tooltip:
			addAttr("title="+values[0]);
			break;
		case Lexer._width:
			addStyle("width:"+values[0]);
			break;
		case Lexer._closable:
			currentContent += "<span onclick=\"this.parentElement.style.display='none'\" class=\"w3-button w3-display-right\">&times</span>";
			break;
		case Lexer._delay:
		case Lexer._slide_controls:
		case Lexer._indicators:
		case Lexer._caption_position:
		case Lexer._caption:
		case Lexer._selected:
			// if parent is checkbox ... else if is radiobutton ...
		}
	}
	
	/** Termina de escribir la etiqueta de apertura para proceder al contenido */
	public void finishOpenTag() {
		output.putString(tabs()+currentLine+"\n");
	}
	
	/** Escribe contenido entre las etiquetas */
	public void genContent(String s) {
		output.putString(s);
	}
	
	/** Cierra la etiqueta abierta */
	public void closeTag() {
		if(currentContent.length() > 0) {
			output.putLine(tabs(1)+currentContent);
			currentContent = "";
		}
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
	public String tabs() {
		String s = "";
		for(int i=0; i<containerStack.size(); i++) {
			s += "    ";
		}
		return s;
	}
	
	
	/** Devuelve n numero de tabs mas del actual de la linea */
	public String tabs(int n) {
		String s = "";
		for(int i=0; i<containerStack.size() + n; i++) {
			s += "    ";
		}
		return s;
	}
	
	/** Introduce un atributo dentro del atributo class */
	public void addClass(String attr) {
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
	public void addStyle(String attr) {
		// Comprobamos que se haya declarado el atributo style y, si no, lo creamos
		if(!currentLine.contains("style=\"")) {
			currentLine = currentLine.substring(0, currentLine.length()-1) + " style=\"" + attr + ";\">";
		}else {	
			String[] sp = currentLine.split("style=\"");
			if(sp.length == 2) {
				currentLine = sp[0] + "style=\"" + attr + "; " + sp[1];
			}
		}
	}
	
	
	/** Agrega un atributo a la etiqueta actual*/
	public void addAttr(String attr) {
		currentLine = currentLine.substring(0, currentLine.length()-1)+" "+attr+">";
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
	
	/** Obtiene la etiqueta actual */
	private String getCurrent() {
		String current = "";
		if(containerStack.size() >= 1) {
			return containerStack.peekFirst()[0];
		}
		return current;
	}
	
	/** Obtiene la etiqueta padre de la actual */
	private String getParent() {
		String parent = "";
		if(containerStack.size() >= 2) {
			String[] current = containerStack.pop();
			parent = containerStack.peekFirst()[0];
			containerStack.push(current);
		}
		return parent;
	}
	
	/** Obtiene la etiqueta que contiene a la etiqueta padre */
	private String getGrandParent() {
		String grandParent = "";
		if(containerStack.size() >= 3) {
			String[] current = containerStack.pop();
			String[] parent = containerStack.pop();
			grandParent = containerStack.peekFirst()[0];
			containerStack.push(parent);
			containerStack.push(current);
		}
		return grandParent;
	}
	
	
}
