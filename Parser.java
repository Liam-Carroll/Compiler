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
                }
                symbol = s.nextToken();//may not catch error here
                
            }
        }else{
            error("identifier_list:: no identifier in list");
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
        }
    }
    private void type() {
        if (symbol.tokenType == T.INTEGER){
            symbol = s.nextToken();
        }else{
            error("type::Missing type INTEGER");
        }
    }
    private void subprogram_declarations() {
        subprogram_declaration();
        if (symbol.tokenType == T.SEMI){
            symbol = s.nextToken();
            subprogram_declarations();
        }
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
                }
            }else{
                error("subprogram_head::Missing Identifier after procedure");
            }
            
        }else{
            error("subprogram_head::Missing 'procedure' declaration");
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
            }
        }else{
            error("arguments::Missing left parenthesis before parameter list");
        }
    }
    //LIAM ABOVE THIS
    //BOBERT AND KYLE BELOW THIS
    private void parameter_list() {
        
    }
    private void compound_statement() {
        
    }
    private void statement_list() {
        
    }
    private void statement() {
        
    }
    private void assignment_statement() {
        
    }
    private void if_statement() {
        
    }
    private void while_statement() {
        
    }
    private void procedure_statement() {
        
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