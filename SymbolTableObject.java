public class SymbolTableObject {
	public String name;
	public int scope;
	
	
	public SymbolTableObject(String name, int scope) {
		this.name = name;
		this.scope = scope;
	}
	
	public SymbolTableObject() {
		this.name = "Name Placeholder";
		this.scope = 0;
	}
}
