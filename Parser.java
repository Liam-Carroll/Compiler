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
            s.customError("ERROR -- Parser Constructor::Scanner Init::problem with filename passed.", s.line);
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
    private void program() {
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
                s.customError("ERROR -- No period to end program", s.line);
            }
        }
    }
    private void identifier_list() {
        if (symbol.tokenType == T.IDENTIFIER){
            symbol = s.nextToken();
            while (symbol.tokenType == T.COMMA){
                Token newIndentifier = s.nextToken();
                if (newIndentifier.tokenType != T.IDENTIFIER){
                    error("identifier_list:: no Identifier after comma seperating ids in list");
                    s.customError("ERROR -- ", s.line);
                }
                symbol = s.nextToken();//may not catch error here
                
            }
        }else{
            error("identifier_list:: no identifier in list");
            s.customError("ERROR -- identifier_list:: no identifier in list", s.line);
        }
    }
    private void variable_declarations() {
        if (symbol.tokenType == T.VAR){
            symbol = s.nextToken();
            variable_declaration();
            if (symbol.tokenType == T.SEMI){
                symbol = s.nextToken();
                Token closingSemiToken = symbol;
                do{
                    variable_declaration();
                }while(closingSemiToken == T.SEMI);
            }else {
                error("variable_declarations::Missing ';' to end variable declaration");
                s.customError("ERROR -- variable_declarations::Missing ';' to end variable declaration", s.line);
            }

        }
    }
    private void variable_declaration() {
        identifier_list();
        if (symbol.tokenType == T.COLON){
            symbol = s.nextToken();
            type();
        }else{
            error("variable_declaration::Missing ':' between identifier_list and type");
            s.customError("ERROR -- variable_declaration::Missing ':' between identifier_list and type", s.line);
        }
    }
    private void type() {
        if (symbol.tokenType == T.INTEGER){
            symbol = s.nextToken();
        }else{
            error("type::Missing type INTEGER");
            s.customError("ERROR -- type::Missing type INTEGER", s.line);
        }
    }
    private void subprogram_declarations() {
        subprogram_declaration();
        if (symbol.tokenType == T.SEMI){
            symbol = s.nextToken();
            subprogram_declarations();
        }//no else bc optional
    }
    private void subprogram_declaration() {
        subprogram_head();
        variable_declarations();
        compound_statement();
    }
    private void subprogram_head() {
        if (symbol.tokenType == T.PROCEDURE){
            symbol = s.nextToken();
            if (symbol.tokenType == T.IDENTIFIER){
                symbol = s.nextToken();
                arguments();
                if (symbol.tokenType == T.SEMI){
                    symbol = s.nextToken();
                }else{
                    error("subprogram_head::Missing semicolon ';' after arguments");
                    s.customError("ERROR -- subprogram_head::Missing semicolon ';' after arguments", s.line);
                }
            }else{
                error("subprogram_head::Missing Identifier after procedure");
                s.customError("ERROR -- subprogram_head::Missing Identifier after procedure", s.line);
            }
            
        }else{
            error("subprogram_head::Missing 'procedure' declaration");
            s.customError("ERROR -- subprogram_head::Missing 'procedure' declaration", s.line);
        }
    }
    private void arguments() {
        if (symbol.tokenType == T.LPAREN){
            symbol = s.nextToken();
            parameter_list();
            if (symbol.tokenType == T.RPAREN){
                symbol = s.nextToken();  
            }else{
                error("arguments::Missing right parenthesis after parameter list");
                s.customError("ERROR -- arguments::Missing right parenthesis after parameter list", s.line);
            }
        }else{
            error("arguments::Missing left parenthesis before parameter list");
            s.customError("ERROR -- arguments::Missing left parenthesis before parameter list", s.line);
        }
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
    private void statement_list() {// ----------------------------------------------idk what goes in here?
        
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