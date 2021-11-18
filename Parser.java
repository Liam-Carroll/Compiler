

import java.io.IOException;
import java.util.Scanner;

public class Parser{
    CompilerScanner s;
    Token symbol;
    ErrorLogger logger;
    Quads quads;
    int scope = 0;


    public Parser() throws Exception{
        quads = new Quads();
        logger = new ErrorLogger("ParserErrorLog.txt");
        Scanner input = new Scanner(System.in);
        System.out.print("Enter program file name: ");
        String fileName = input.nextLine();
        //init scanner
        try {
            s = new CompilerScanner(fileName);
        } catch (IOException e) {
            System.out.println("Error: Parser Constructor::Scanner Init::problem with filename passed.");
            logger.customError("ERROR -- Parser Constructor::Scanner Init::problem with filename passed.", s.line);
            e.printStackTrace();
        }
        input.close();
        //read first token
        symbol = new Token();
        symbol = s.newNextToken();
        //move to start method
        // System.out.println("VERY First token: " + symbol.tokenType);
        program();
    }
    private void error(String string) {
        System.out.println(string);
    }
    private void program() throws Exception {
        Token firstToken = symbol;                                              //Program
        //System.out.println("First token: " + firstToken.tokenType);
        Token secondToken = s.nextToken();                                      //Add
        //System.out.println("Second token: " + secondToken.tokenType);
        Token thirdToken = s.nextToken();                                       //;
        // System.out.println("Third token: " + thirdToken.tokenType);
        // System.out.println("First Token: "+firstToken.tokenType+"\nSecond token: " + secondToken.tokenType+"\nThird token: " + thirdToken.tokenType);
        
        if (firstToken.tokenType == T.PROGRAM && secondToken.tokenType == T.IDENTIFIER && thirdToken.tokenType == T.SEMI) {
        	//System.out.println("ENTERS");
            try{
            	s.symbolTable.table[s.symbolTable.currentAdd].name = secondToken.name;
            	s.symbolTable.table[s.symbolTable.currentAdd].kind = "program_name";
            	s.symbolTable.table[s.symbolTable.currentAdd].type = T.PROGRAM;
            	s.symbolTable.table[s.symbolTable.currentAdd].scope = 0;
            	s.symbolTable.table[s.symbolTable.currentAdd].declared = "true";
                //Debug
                quads.insertQuad("DCL", null, null, "0");
                //progresses to next token
                symbol = s.newNextToken();
                variable_declarations();
                subprogram_declarations();
                compound_statement();
                // symbol = s.newNextToken();//CHANGED THIS TO BELOW
                if (symbol.tokenType != T.PERIOD){
                    logger.customError("ERROR -- No period to end program", s.line);
                }
                s.symbolTable.print();
            }catch(Exception e){
                e.printStackTrace();
                // System.out.println("First Token: "+firstToken.tokenType+"\nSecond token: " + secondToken.tokenType+"\nThird token: " + thirdToken.tokenType);
            } 
        } else {
        	logger.customError("ERROR -- MISSING PROGRAM DECLARATION LINE", 0);
        }
    }
    private void identifier_list(Semantics sem) throws Exception {
    	sem.count = 0;
        if (symbol.tokenType == T.IDENTIFIER) {
        	sem.start = s.symbolTable.currentAdd;
        	sem.count++;
            symbol = s.newNextToken();
            while (symbol.tokenType == T.COMMA) {
                symbol = s.newNextToken();
                if (symbol.tokenType == T.IDENTIFIER) {
                	sem.count++;
                	symbol = s.newNextToken();
                } else {
                	logger.customError("ERROR -- EXPECTING ID", s.line);
                }
            }
        }else{
            logger.customError("ERROR -- identifier_list:: no identifier in list", s.line);
        }
    }
    private void variable_declarations() throws Exception { // pretty sure this doesnt work you dont need to make extra tokens it messes up the current token
    	if (symbol.tokenType == T.VAR) {
            symbol = s.newNextToken();//pointing at an ID
            Semantics sem = new Semantics();
            variable_declaration(sem);
            for (int j = sem.start; j < sem.count; j++) {
                quads.insertQuad("DCL", null, null, ""+j);
            	s.symbolTable.table[j].type = sem.type;
            }
            if (symbol.tokenType == T.SEMI) {
                symbol = s.newNextToken();
                while (symbol.tokenType == T.IDENTIFIER) {
                	variable_declaration(sem);
                	for (int j = sem.start; j < sem.count; j++) {
                        quads.insertQuad("DCL", null, null, ""+j);
                    	s.symbolTable.table[j].type = sem.type;
                    }
                	if (symbol.tokenType == T.SEMI) {
                		symbol = s.newNextToken();
                	} else {
                		logger.customError("ERROR -- Expecting ';'", s.line);
                	}
                }

            }else {
                logger.customError("ERROR -- variable_declarations::Missing ';' to end variable declaration", s.line);
            }
        }

    }
    
    private void variable_declaration(Semantics sem) throws Exception {
        identifier_list(sem);
        if (symbol.tokenType == T.COLON){
            symbol = s.newNextToken();
            type(sem);
        }else{
            logger.customError("ERROR -- variable_declaration::Missing ':' between identifier_list and type", s.line);
        }
    }
    
    private void type(Semantics sem) throws Exception {
        if (symbol.tokenType == T.INTEGER){
            symbol = s.newNextToken();
            sem.type = T.INTEGER;
        }else{
            logger.customError("ERROR -- type::Missing type INTEGER", s.line);
        }
    }
    
    private void subprogram_declarations() throws Exception {
        if (symbol.tokenType == T.PROCEDURE) {
        	subprogram_declaration();
            if (symbol.tokenType == T.SEMI){
                symbol = s.newNextToken();
                subprogram_declarations();
            } else {
            	logger.customError("ERROR -- Expecting ';'", s.line);
            }
        }
    }
    private void subprogram_declaration() throws Exception {
    	s.incScope();
        subprogram_head();
        variable_declarations();
        compound_statement();
        s.decScope();
    }
    private void subprogram_head() throws Exception {
        if (symbol.tokenType == T.PROCEDURE) {
            symbol = s.newNextToken();
            if (symbol.tokenType == T.IDENTIFIER) {
                symbol = s.newNextToken();
                arguments();
                if (symbol.tokenType == T.SEMI) {
                    symbol = s.newNextToken();
                }else{
                    logger.customError("ERROR -- subprogram_head::Missing semicolon ';' after arguments", s.line);
                }
            }else{
                logger.customError("ERROR -- subprogram_head::Missing Identifier after procedure", s.line);
            }
            
        }else{
            logger.customError("ERROR -- subprogram_head::Missing 'procedure' declaration", s.line);
        }
    }
    private void arguments() throws Exception {
        if (symbol.tokenType == T.LPAREN) {
            symbol = s.newNextToken();
            parameter_list();
            if (symbol.tokenType == T.RPAREN) {
                symbol = s.newNextToken();  
            }else{
                logger.customError("ERROR -- arguments::Missing right parenthesis after parameter list", s.line);
            }
        }else{
            logger.customError("ERROR -- arguments::Missing left parenthesis before parameter list", s.line);
        }
    }
    private void parameter_list() throws Exception{
    	Semantics sem = new Semantics();
    	
        identifier_list(sem);
        if(symbol.tokenType == T.COLON) {
        	symbol = s.newNextToken();
            type(sem);
            while (symbol.tokenType == T.SEMI) {
            	symbol = s.newNextToken();
            	identifier_list(sem);
            	if (symbol.tokenType == T.COLON) {
            		symbol = s.newNextToken();
            		type(sem);
            	} else {
            		logger.customError("ERROR -- Expecting ':'", s.line);
            	}
            }
            
        } else {
        	logger.customError("ERROR -- EXPECTING COLON", s.line);
        }
    }
    private void compound_statement() throws Exception {
        if (symbol.tokenType == T.BEGIN) {
            symbol = s.newNextToken();
            statement_list();
            if (symbol.tokenType == T.END) {
                symbol = s.newNextToken();
            }else{
                logger.customError("ERROR -- compound_statement::EXPECTING END  after statement_list in compound statement", s.line);
            }

        }else{
            logger.customError("ERROR -- compound_statement::EXPECTING BEGIN before statement_list in compound statement", s.line);
        }
    }
    private void statement_list() throws Exception{// ----------------------------------------------idk what goes in here?
        statement();
        while(symbol.tokenType == T.SEMI) {
            symbol = s.newNextToken();
            statement();
        }
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
        } else if (symbol.tokenType == T.WRITELN) {
        	writeln_statement();
        }

    }
    private void assignment_statement() throws Exception {
    	if (symbol.tokenType == T.IDENTIFIER) {
    		symbol = s.newNextToken();
    		if (symbol.tokenType == T.ASSIGN) {
    			symbol = s.newNextToken();
            	expression();
            } else {
            	logger.customError("ERROR -- EXPECTING ASSIGNMENT OP", s.line);
            }
    	} else {
    		logger.customError("ERROR -- EXPECTING IDENTIFIER", s.line);
    	}
    }
    private void if_statement() throws Exception {
        if (symbol.tokenType == T.IF) {
        	symbol = s.newNextToken();
        	expression();
        	if (symbol.tokenType == T.THEN) {
        		symbol = s.newNextToken();
        		statement();
        		if (symbol.tokenType == T.ELSE) {
        			symbol = s.newNextToken();
        			statement();
        		}
        	} else {
        		logger.customError("ERROR -- EXPECTING THEN", s.line);
        	}
        } else {
        	logger.customError("ERROR -- EXPECTING IF STATEMENT", s.line);
        }
    }
    private void while_statement() throws Exception{
        if(symbol.tokenType == T.WHILE) {
        	symbol = s.newNextToken();
            expression();
            if(symbol.tokenType == T.DO) {
            	symbol = s.newNextToken();
            	statement();
            } else {
            	logger.customError("ERROR -- EXPECTING DO", s.line);
            }
        } else {
        	logger.customError("ERROR -- EXPECTING WHILE", s.line);
        }
    }
    private void procedure_statement() throws Exception {
        if (symbol.tokenType == T.CALL) {
        	symbol = s.newNextToken();
        	if (symbol.tokenType == T.IDENTIFIER) {
        		symbol = s.newNextToken();
        		if (symbol.tokenType == T.LPAREN) {
        			symbol = s.newNextToken();
        			expression_list();
        			if (symbol.tokenType == T.RPAREN) {
        				symbol = s.newNextToken();
        			} else {
        				logger.customError("ERROR -- EXPECTING RIGHT PARAM", s.line);
        			}
        		} else {
        			logger.customError("ERROR -- EXPECTING LEFT PAREN", s.line);
        		}
        	} else {
        		logger.customError("ERROR -- EXPECING IDENTIFIER", s.line);
        	}
        } else {
        	logger.customError("ERROR -- EXPECTING CALL", s.line);
        }
    }
    private void expression_list() throws Exception{
        expression();
        while(symbol.tokenType == T.COMMA) {
            symbol = s.newNextToken();
            expression();
        }
    }
    private void expression() throws Exception {
    	simple_expression();
        if ((symbol.tokenType == T.LT) || (symbol.tokenType == T.GT) || (symbol.tokenType == T.EQUAL) || (symbol.tokenType == T.GE) || (symbol.tokenType == T.LE) || (symbol.tokenType == T.NE)) {
        	symbol = s.newNextToken();
        	simple_expression();
        }
    }
    private void simple_expression() throws Exception {
    	if (symbol.tokenType == T.MINUS) {
    		symbol = s.newNextToken();
    	}
        term();
        while ((symbol.tokenType == T.PLUS) || (symbol.tokenType == T.MINUS) || (symbol.tokenType == T.OR)) {
        	symbol = s.newNextToken();
        	term();
        }
    }
    private void term() throws Exception {
        factor();
        while ((symbol.tokenType == T.TIMES) || (symbol.tokenType == T.DIV)|| (symbol.tokenType == T.MOD) || (symbol.tokenType == T.AND)) {
        	symbol = s.newNextToken();
        	factor();
        }
    }
    private void factor() throws Exception{
        // I dont know what to do for true and false terminals
        // there is no T.TRUE or T.FALSE - bobby
    	// replaced true and false with boolean, please fix if wrong - kyle
        if(symbol.tokenType == T.IDENTIFIER) 
            symbol = s.newNextToken();
        else if(symbol.tokenType == T.NUMBER) 
            symbol = s.newNextToken();
        else if (symbol.tokenType == T.BOOL) 
        	symbol = s.newNextToken();
        else if(symbol.tokenType == T.LPAREN) {
        	symbol = s.newNextToken();
            expression();
           if (symbol.tokenType == T.RPAREN) {
        	   symbol = s.newNextToken();
           } else {
        	   logger.customError("ERROR -- EXPECTING RIGHT PAREN", s.line);
           }
        }
        else if(symbol.tokenType == T.NOT) {
        	symbol = s.newNextToken();
            factor();
        }
        else {
        	logger.customError("ERROR -- EXPECING SOME KIND OF FACTOR", s.line);
        }

    }
    private void read_statement() throws Exception{
        if(symbol.tokenType == T.READ) {
            symbol = s.newNextToken();
            if(symbol.tokenType == T.LPAREN) {
            	symbol = s.newNextToken();
                input_list();
        		if (symbol.tokenType == T.RPAREN) {
        			symbol = s.newNextToken();
        		} else {
        			logger.customError("ERROR -- EXPECTING RIGHT PAREN", s.line);
        		}
            } else {
            	logger.customError("ERROR -- EXPECTING LEFT PAREN", s.line);
            }
        } else {
        	logger.customError("ERROR -- EXPECTING READ", s.line);
        }
    }
    private void write_statement() throws Exception{
    	if(symbol.tokenType == T.WRITE) {
            symbol = s.newNextToken();
            if(symbol.tokenType == T.LPAREN) {
            	symbol = s.newNextToken();
                output_item();
        		if (symbol.tokenType == T.RPAREN) {
        			symbol = s.newNextToken();
        		} else {
        			logger.customError("ERROR -- EXPECTING RIGHT PAREN", s.line);
        		}
            } else {
            	logger.customError("ERROR -- EXPECTING LEFT PAREN", s.line);
            }
        } else {
        	logger.customError("ERROR -- EXPECTING WRITE", s.line);
        }
    }
    private void writeln_statement() throws Exception{
    	if(symbol.tokenType == T.WRITELN) {
            symbol = s.newNextToken();
            if(symbol.tokenType == T.LPAREN) {
            	symbol = s.newNextToken();
                output_item();
        		if (symbol.tokenType == T.RPAREN) {
        			symbol = s.newNextToken();
        		} else {
        			logger.customError("ERROR -- EXPECTING RIGHT PAREN", s.line);
        		}
            } else {
            	logger.customError("ERROR -- EXPECTING LEFT PAREN", s.line);
            }
        } else {
        	logger.customError("ERROR -- EXPECTING WRITELN", s.line);
        }
    }
    private void output_item() throws Exception{
        if(symbol.tokenType == T.STRING) {
        	symbol = s.newNextToken();
        } else if ((symbol.tokenType == T.MINUS) || (symbol.tokenType == T.IDENTIFIER) || (symbol.tokenType == T.NUMBER) || (symbol.tokenType == T.BOOL) || (symbol.tokenType == T.LPAREN) || (symbol.tokenType == T.NOT)) {
        	expression();
        } else {
        	logger.customError("ERROR -- Missing expression", s.line);
        }
        
    }
    private void input_list() throws Exception{// I dont know if this is correct
        if(symbol.tokenType == T.IDENTIFIER) {
            symbol = s.newNextToken();
            while(symbol.tokenType == T.COMMA) {
                symbol = s.newNextToken();
                if (symbol.tokenType == T.IDENTIFIER) {
                	symbol = s.newNextToken();
                } else {
                	logger.customError("ERROR -- EXPECTING IDENTIFIER", s.line);
                }
            }
        } else {
        	logger.customError("ERROR -- EXPECTING IDENTIFIER", s.line);
        }
    }



    public static void main(String[] args) throws Exception{
        Parser p = new Parser();
        
    }
}