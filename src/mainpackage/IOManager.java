package mainpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class IOManager {

	private File file = null;
	private FileReader fr = null;
	private BufferedReader br = null;
	private FileWriter fw = null;
	private PrintWriter pw = null;
	private long lineNumber = 0;
	
	/** Abre un fichero en modo de lectura o escritura
	 * Si el fichero se abre en escritura y éste existe, entonces
	 * se sobreescribe. */
	public void openForRead(String path, boolean internalFile) throws IOException {
		lineNumber = 0;
		if(internalFile) {
			InputStream is = getClass().getResourceAsStream(path);
			br = new BufferedReader(new InputStreamReader(is));
		}else {
			file = new File(path);
			fr = new FileReader(file);
			br = new BufferedReader(fr);
		}

	}
	
	public void openForWrite(String path) {
		lineNumber = 0;
		File file = new File(path);
		try {
			fw = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw = new PrintWriter(fw);
	}
	
	/** Lee una línea de un fichero abierto para lectura */
	public String getLine() throws IOException {
		if(br != null) {
			lineNumber++;
			return br.readLine();
		}else {
			System.err.println("File opened only for writing");
			return null;
		}
	}
	
	/** Escribe una línea en un fichero abierto para escritura */
	public void putLine(String val) {
		if(pw != null) {
			lineNumber++;
			pw.println(val);
		}else {
			System.err.println("File opened only for reading");
		}
	}
	
	/** Escribe una línea en un fichero abierto para escritura */
	public void putString(String val) {
		if(pw != null) {
			lineNumber++;
			pw.print(val);
		}else {
			System.err.println("File opened only for reading");
		}
	}
	
	/** Devuleve el número de línea del fichero que se está leyendo o escribiendo */
	public long getLineNumber() {
		return lineNumber;
	}
	
	/** Cierra los ficheros que se hayan abierto */
	public void close() throws IOException {
		if(fr != null) {
			fr.close();
		}
		if(fw != null) {
			fw.close();
		}
		if(pw != null) {
			pw.close();
		}
	}
	
	
	/** Elimina un fichero */
	public void deleteFile(String path) {
		File fichero = new File(path);
		fichero.delete();
	}
	
}
