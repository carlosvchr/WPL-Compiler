package mainpackage;

public class CompResult {

	boolean compilationSuccessfully;
	String compilationOutput;
	
	public CompResult() {
		compilationOutput = "";
	}
	
	public void add(String s) {
		if(compilationOutput.length()!=0)
			compilationOutput += "\n";
		compilationOutput += s;
	}
	
	public void setResult(boolean b) {
		compilationSuccessfully = b;
	}
	
	public String output() {
		return compilationOutput;
	}
	
	public boolean isCompilationSuccessfully() {
		return compilationSuccessfully;
	}
	
}
