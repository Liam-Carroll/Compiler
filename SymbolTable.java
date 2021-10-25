package Scanner;

public class SymbolTable {
	private SymbolTableObject[] table;
	
	/**
	 * Default constructor
	 */
	public SymbolTable() {
		table = new SymbolTableObject[100];
	}
	
	/**
	 * Search
	 * @param name = the symbol to search for
	 * @param scope = the symbols scope
	 * @return location of the symbol in the table
	 */
	public int search(String name, int scope) {
		for (int i = 0; i < table.length; i++) {
			if (table[i].name.equals(name)) {
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
	public int insert(String name, int scope) {
		SymbolTableObject newSymbol = new SymbolTableObject(name, scope);
		for (int i = 0; i < table.length; i++) {
			if (table[i] == null) {
				table[i] = newSymbol;
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
	
	
}
