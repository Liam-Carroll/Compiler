package Scanner;

public class SymbolTable {
	public SymbolTableObject[] table;
	public int currentAdd;
	
	/**
	 * Default constructor
	 */
	public SymbolTable() {
		table = new SymbolTableObject[100];
		currentAdd = 0;
//		for (int i = 0; i < table.length; i++) {
//			table[i] = new SymbolTableObject();
//		}
	}
	
	/**
	 * Search
	 * @param name = the symbol to search for
	 * @param scope = the symbols scope
	 * @return location of the symbol in the table
	 */
	public int search(String name, int scope) {
		for (int i = 0; i < table.length; i++) {
			if (table[i].name.equals(name) && table[i].scope == scope) {
				return i;
			}
		}
		return -1; // not found in table
	}
	
	/**
	 * 
	 * @param name = name of symbol to store
	 * @param scope = scope of the symbol to store
	 * @return returns the location in the table (may already be stored in table)
	 */
	public int insert(String name, String kind, int type, int scope, String declared) {
		SymbolTableObject newSymbol = new SymbolTableObject(name, kind, type, scope, declared);
		for (int i = 0; i < table.length; i++) {
			if (table[i] == null) {
				table[i] = newSymbol;
				//System.out.println("success in " + i);
				currentAdd = i;
				return i;
			}
			else if (table[i].name.equals(name) && table[i].scope == scope) {
				//Symbol is already in table, return the index
				return i;
			}
		}
		System.out.println("ERROR");
		return -1; // error, could not find empty location or already existing symbol
	}
	
	public void print() {
		for (int i = 0; i<table.length;i++) {
			if (table[i] == null)
				System.exit(0);
			System.out.println("Name: " + table[i].name +
					"\t Kind: " + table[i].kind + 
					"\t Type: " + table[i].type + 
					"\t Scope: " + table[i].scope +
					"\t Declared: " + table[i].declared);
		}
	}
	
	
}
