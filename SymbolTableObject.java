

public class SymbolTableObject {
	public String name;
	public String kind;
	public int type;
	public int scope;
	public String declared;
	public int offset;
	
	
	public SymbolTableObject(String name, String kind, int type, int scope, String declared, int offset) {
		this.name = name;
		this.kind = kind;
		this.type = type;
		this.scope = scope;
		this.declared = declared;
		this.offset = offset;
	}
	
	public SymbolTableObject() {
		this.name = "Empty";
		this.kind = "Empty";
		this.type = -10;
		this.scope = -10;
		this.declared = "Empty";
		this.offset = -10;
	}
	
	public SymbolTableObject(String name) {
		this.name = name;
		this.kind = "Empty";
		this.type = -10;
		this.scope = -10;
		this.declared = "Empty";
		this.offset = -10;
	}
}
