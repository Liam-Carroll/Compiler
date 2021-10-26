package Scanner;

import java.io.IOException;
import java.util.Scanner;

public class Parser{
    CompilerScanner s;
    Token symbol;

    public Parser() throws Exception{
        Scanner input = new Scanner(System.in);
        System.out.print("Enter program file name: ");
        String fileName = input.nextLine();
        //init scanner
        try {
            s = new CompilerScanner(fileName);
        } catch (IOException e) {
            System.out.println("Error: Parser Constructor::Scanner Init::problem with filename passed.");
            e.printStackTrace();
        }
        input.close();
        //read first token
        symbol = s.nextToken();
        //move to start method
        program();
    }
    private void error(String string) {
        System.out.println(string);
    }
    private void program() throws Exception {
        Token firstToken = symbol;
        Token secondToken = s.nextToken();
        Token thirdToken = s.nextToken();

        if (firstToken.tokenType == T.PROGRAM && secondToken.tokenType == T.IDENTIFIER && thirdToken.tokenType == T.SEMI){
            symbol = s.nextToken();//progresses to next token

            variable_declarations();
            subprogram_declarations();
            compound_statement();
            if (symbol.tokenType != T.PERIOD){
                error("No period to end program");
            }
        }
    }
    private void identifier_list() throws Exception {
        if (symbol.tokenType == T.IDENTIFIER){
            symbol = s.nextToken();
            while (symbol.tokenType == T.COMMA){
                Token newIndentifier = s.nextToken();

            }
        }
    }
    private void variable_declarations() {
        
    }
    private void variable_declaration() {
        
    }
    private void type() {
        
    }
    private void subprogram_declarations() {
        
    }
    private void subprogram_declaration() {
        
    }
    private void subprogram_head() {
        
    }
    private void arguments() {
        
    }
    //LIAM ABOVE THIS
    //BOBERT AND KYLE BELOW THIS
    private void parameter_list() {

    }
    private void compound_statement() throws Exception {
        if (symbol.tokenType == T.BEGIN) {
        	statement_list();
        	symbol = s.nextToken();
        	if (symbol.tokenType != T.END) {
        		s.customError("ERROR -- EXPECTING END", s.line);
        	}
        }
    }
    private void statement_list() { // ----------------------------------------------idk what goes in here?
        
    }
    private void statement() throws Exception { // not done
        if (symbol.tokenType == T.IDENTIFIER) {
        	assignment_statement();
        }
        else if (symbol.tokenType == T.CALL) {
        	procedure_statement();
        }
        else if (symbol.tokenType == T.BEGIN) {
        	compound_statement();
        }
        else if (symbol.tokenType == T.IF) { 
        	if_statement();
        }
        else if (symbol.tokenType == T.WHILE) {
        	while_statement();
        }
        else if (symbol.tokenType == T.READ) {
        	read_statement();
        }
        else if (symbol.tokenType == T.WRITE) {
        	write_statement();
        }
       
    }
    private void assignment_statement() throws Exception {
    	if (symbol.tokenType == T.IDENTIFIER) {
    		symbol = s.nextToken();
    		if (symbol.tokenType == T.ASSIGN) {
            	expression();
            }
    	}
    }
    private void if_statement() throws Exception { // -------------------------------------------- liam check this, the else brackets is that right?
        if (symbol.tokenType == T.IF) {
        	expression();
        	symbol = s.nextToken();
        	if (symbol.tokenType == T.THEN) {
        		statement();
        		symbol = s.nextToken();
        		if (symbol.tokenType == T.ELSE) {
        			statement();
        		}
        	} else {
        		s.customError("ERROR -- EXPECTING THEN", s.line);
        	}
        }
    }
    private void while_statement() {
        
    }
    private void procedure_statement() throws Exception {
        if (symbol.tokenType == T.CALL) {
        	symbol = s.nextToken();
        	if (symbol.tokenType == T.IDENTIFIER) {
        		symbol = s.nextToken();
        		if (symbol.tokenType == T.LPAREN) {
        			expression_list();
        			symbol = s.nextToken();
        			if (symbol.tokenType != T.RPAREN) {
        				s.customError("ERROR -- EXPECTING RIGHT PARAM", s.line);
        			}
        		}
        	}
        }
    }
    private void expression_list() {
        
    }
    private void expression() throws Exception {
    	symbol = s.nextToken();
    	simple_expression();
        //if (symbol.tokenType == T.RE) // --------------------------------------------- NO T.RELOP WHAT DO HELP
    }
    private void simple_expression() throws Exception { // -------------------------- confused about the minus here. is this correct?
        symbol = s.nextToken();
        term();
        symbol = s.nextToken();
        while (symbol.tokenType == T.PLUS) {
        	term();
        	symbol = s.nextToken();
        }
    }
    private void term() throws Exception {
        symbol = s.nextToken();
        factor();
        symbol = s.nextToken();
        while (symbol.tokenType == T.TIMES) {
        	factor();
        	symbol = s.nextToken();
        }
    }
    private void factor() {
        
    }
    private void read_statement() {
      
    }
    private void write_statement() {
        
    }
    private void writeln_statement() {
        
    }
    private void output_item() {
        
    }
    private void input_list() {
        
    }



    public static void main(String[] args) throws Exception{
        Parser p = new Parser();
    }
}