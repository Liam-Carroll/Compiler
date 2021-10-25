package Scanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class Parser {
	public CompilerScanner myScanner;
	public SymbolTable symbolTable;
	public StringTable stringTable;
	public String fileName;
	public Token tok;
	
	/**
	 * Constructor
	 * @param filename = name of file to parse
	 * @throws Exception
	 */
	public Parser (String filename) throws Exception {
		this.symbolTable = new SymbolTable();
		this.stringTable = new StringTable();
		this.fileName = filename;
		this.myScanner = new CompilerScanner(filename, stringTable, symbolTable);
	}
	
	/**
	 * Prints the symbol table out to a file
	 * @throws Exception
	 */
	public void writeSymbolTable() throws Exception {
        PrintWriter out = new PrintWriter("SymbolTableOut.txt");
        for (int i = 0; i < symbolTable.table.length; i++) {
        	if (symbolTable.table[i] != null)
        		out.write("" + symbolTable.table[i].name);
        }
        out.close();
	}
	
	/**
	 * Variable Declarations
	 */
	public void variableDeclarations() throws Exception {
		System.out.println("Variable Declarations");
		//code for method
	}
	
	public void variableDeclaration() throws Exception {
		//code for method
	}
	
	/**
	 * Parse function
	 * @throws Exception
	 */
	public void parse() throws Exception {
		System.out.println("Program");
		if (tok.tokenType == T.PROGRAM) {
			tok = myScanner.nextToken();
		} else {
			myScanner.error(15, myScanner.line);
		}
		
		if (tok.tokenType != T.IDENTIFIER) {
			myScanner.error(16, myScanner.line);
		} else {
			tok = myScanner.nextToken();
		}
		
		if(tok.tokenType == T.SEMI) {
			tok = myScanner.nextToken();
		} else {
			myScanner.error(17, myScanner.line - 1);
		}
		
		variableDeclarations();
		
		subprogramDeclarations();
		
		compoundDeclarations();
		
		if (tok.tokenType == T.PERIOD) {
			System.out.println("Success");
		} else {
			myScanner.error(18, myScanner.line - 1);
		}
	}
	
	public void subprogramDeclarations() {
		// code goes here
	}
	
	public void compoundDeclarations() {
		//code goes here
	}
	
	public static void main (String args[]) throws Exception {
		Scanner input = new Scanner(System.in);
		System.out.print("Enter filename: ");
		String filen = input.next();
		Parser p = new Parser(filen);
		p.tok = p.myScanner.nextToken();
		p.parse();
		p.writeSymbolTable();
	}
	
}
