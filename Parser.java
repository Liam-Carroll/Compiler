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
    private void compound_statement() {
        
    }
    private void statement_list() {
        
    }
    private void statement() throws Exception { // not done
        if (symbol.tokenType == T.IDENTIFIER) {
        	assignment_statement();
        }
        else if (symbol.tokenType == T.CALL) {
        	procedure_statement();
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
    private void if_statement() {
        
    }
    private void while_statement() {
        
    }
    private void procedure_statement() throws Exception { // not done
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
    private void expression() {
        
    }
    private void simple_expression() {
        
    }
    private void term() {
        
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