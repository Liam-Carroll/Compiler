package Scanner;

import java.io.*;
import java.util.Scanner;

public class CompilerScanner
{
    private int[][] fsm;//contains the state mappings of the FSM, read from file
    private String[] reserved ={"program","var","integer","bool","procedure","call","begin","end","if","then","else","while","do","and","or","not","read","write","writeln"};
    private SymbolTable symbolTable = new SymbolTable();
    private StringTable stringTable = new StringTable();

    File f;
    FileReader fileReader;
    BufferedReader br;
    // BufferedReader br2;//for reading error
    PrintWriter out;
    char ch = ' ';
    int line = 0;
    int state = 0;

    public CompilerScanner(String fileName) throws IOException 
    {//Scanner Init
        f = new File(fileName);
        fileReader = new FileReader(f);
        br = new BufferedReader(fileReader);
        // br2 = new BufferedReader(fileReader);
        
        //===================Output to txt==========================
        FileReader fileReader2 = new FileReader(f);
        BufferedReader br3 = new BufferedReader(fileReader2);

        out = new PrintWriter("output.txt");
        int increment = 1; 
        
        String output = br3.readLine();
        while(output != null) {
         //System.out.println(output);
         if(output.isEmpty())
            break; 
         out.write(increment + " " + output + System.lineSeparator());
         output = br3.readLine();
         increment++;
        }
        //==========================================================

        fsm = new int[15][11];

        Scanner input = new Scanner(new File("FSM.txt"));
       // System.out.print("==========FSM=========");
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 11; col++){
                fsm[row][col] = input.nextInt();
                //System.out.print(fsm[row][col]+" ");
            }
            //System.out.println();
        }
        //System.out.println("======================");
        input.close();
    }

    public void error(int errorCode, int line) throws IOException{
        String errorString="";
        switch(errorCode){
            case 13:
                errorString="ERROR -- ILLEGAL CHARACTER";
                break;
            case 14:
                errorString="ERROR -- STRING NOT TERMINATED";
                break;
               //"Final state, scanner error, character.";
            case 15:
            	errorString="ERROR -- EXPECTING PROGRAM";
            	break;
            case 16:
            	errorString="ERROR -- EXPECTING PROGRAM NAME";
            case 17:
            	errorString="ERROR -- EXPECTING SEMI";
            default:
                errorString="ERROR";
                break;
        }
        // String currentLine;
        // int lineNum=0;
        // while ((currentLine = br.readLine()) != null)   {
        //     // Print the content on the console
        //     System.out.println(lineNum+" "+currentLine);
        //     lineNum++;
        // }
        out.write(errorString + " at line "+line);
        //accepts the error msg and line num
        //prints the msg and exits
        //msg is illegal character or string left unterminated by a '
        
    }
    
    public void customError(String errorString, int line ) {
    	System.out.println(errorString + " at line "+line);
    	out.write(errorString + " at line "+line);
    	System.exit(0);
    }

    public String removeSpacesAndComments(String stringPassed){
        String newString="";
        char currentChar;
        // boolean isBeginComment=false;
        stringPassed = stringPassed.replaceAll(System.lineSeparator(), "");
        stringPassed = stringPassed.replaceAll(" ","");
        stringPassed = stringPassed.replaceAll("\n","");
        stringPassed = stringPassed.replaceAll("\r","");
        stringPassed = stringPassed.replaceAll("\f","");
        stringPassed = stringPassed.replaceAll("\t","");
        boolean isNotInString = false;
        for(int i =0; i<stringPassed.length(); i++){
            currentChar=stringPassed.charAt(i);
            if (currentChar == '\'') 
            	isNotInString = isNotInString ? false : true;//toggle every time we see a quote
            
            if(currentChar =='!' && isNotInString){ // we need to fix because if there 
                   return newString;//ignore rest of string as it is commented out
            }
                   newString=newString+currentChar;
        }
        return newString;
        //br.readLine(reads the rest of the line) use for comments

    }
    
    public int getCategory(char ch){
        if ((ch >='A' && ch <= 'Z') || (ch >='a' && ch <= 'z'))
        {
            return 0;//letter
        }
        if (ch >='0' && ch <= '9')
        {
            return 1;//Digit
        }
        switch(ch){
            case '\'':
                return 2;//Quote
            case ':':
                return 3;//colon
            case '\n':
                return 4;//New-line
            case '>':
                return 5;//GT
            case '<':
                return 6;//LT
            case '=':
                return 7;
            case '+':
            case '-':
            case '*':
            case '/':
            case '%':
            case '(':
            case ')':
            case ',':
            case '.':
            case ';':
                return 8;//punction 
            case ' '://whitespace
                return 9;

            default:
                return 10;//ERROR ILLEGAL CHARACTER
        }
    }

    public Token nextToken() throws Exception{
        //removespaces()
        // scanning algorithm
        int state = 0;

        int charClass = getCategory(ch);//column value for the FSM
        // System.out.println(charClass);
        String buf = "";
        // System.out.println("TEXT: "+(char)br.read());
        do{
            buf = buf + ch;
            ch = (char)br.read();//get the next character, ch
            // System.out.println(buf+"-> "+ch);
            state = fsm[state][charClass];
            charClass = getCategory(ch);
            // System.out.println(charClass);
            ///Check end of line?y/n
            br.mark(1000); 
            if(br.read() == '\n'){
                line++;
                // System.out.println("LINE INCREASED::"+line);
            }
            br.reset();

        }while(fsm[state][charClass] > 0);
        //System.out.println("Buf before: " + buf);
        buf = removeSpacesAndComments(buf);
       // System.out.println("Buf after: " + buf);
        if (!buf.equals("")) {
//            throw new Exception(" buf equals nothing");
	        System.out.print(buf+"\t");
	        int tokenType = finalState(state, buf);
	        if (tokenType == 13 || tokenType == 14 || tokenType == 15){
	            error(tokenType, line);
	            return null;
	            // System.exit(1);
	        }
	        Token newToken = new Token(tokenType,0);
	        if (newToken == null) //this code doesnt make any sense it keeps saying symbol is null but i tell it to print if its null and it is dead code so i dont even know
	        	System.out.println("PLEASE HELP SOMETHING IS NULL");
	        return newToken;
        } 
        //System.out.println("Blank Line");
        return null;//null HERE SHOULDNT GET HERE>!>!>!>!>!>!>!>!>>!
        // if state is a final state{
        //     Token t = finalState(state)
        // }else{
        //     error
        // }
    }
    public Token newNextToken() throws Exception {//token is getting returned as null in nextToken even though that is not allowed to happen so this will filter until it doesnt happen
    	Token newToken = nextToken();
    	while (newToken == null) {
    		newToken = nextToken();
    	}
    	return newToken;
    }
    private boolean isResWord(String buf){
        buf = removeSpacesAndComments(buf);
        for (int i=0; i<reserved.length; i++){
            if (buf.equals(reserved[i])){
                //System.out.println(buf + "=" + reserved[i]);
                return true;
            }
        }
        //System.out.println("False on: " + buf);
        return false;
    }
    

    private int finalState(int state, String buf) {
        buf = removeSpacesAndComments(buf);
        switch(state){
            case 1://IDENTIFIER, Possible Reserve
                if (isResWord(buf)){
                    //System.out.println(", "+T.RES);
                    for(int i = 0; i < reserved.length; i ++) {
                        if (buf.equals(reserved[i])){
                            System.out.println(", "+i);
                            return i;
                            
                        }
                    }
                    return 44;//RESERVED WORD
                }//NOT RESERVED W0RD. STORE IN SYMBOL TABLE.
                
                int symbolLocation = symbolTable.insert(buf, 0);
                //System.out.println("Location of " + buf + "=" + location);
                System.out.println(", "+ T.IDENTIFIER + "\t Symbol Location: " + symbolLocation);
                return T.IDENTIFIER;// +", "+buf;//final state ID
            case 2:
                System.out.println(", "+T.NUMBER);
                return T.NUMBER;// +", "+buf;//final state NUMBER
            case 4:
            	int stringLocation = stringTable.insert(buf);
                System.out.println(", "+T.STRING + "\t String Location: " + stringLocation);
                return T.STRING;// +", "+buf;//final state STRING
            case 5:
                if (buf.equals(":=")){
                    System.out.println(", "+T.ASSIGN);
                    return T.ASSIGN;
                }
                System.out.println(", "+T.COLON);
                return T.COLON;// +", "+buf;//final state COLON
            case 6:
                System.out.println(", "+T.ASSIGN );
                return T.ASSIGN;// +", "+buf;//final state ASSIGN
            case 7:
                System.out.println(", "+T.GT );
                return T.GT;// +", "+buf;//final state GT
            case 8:
                System.out.println(", "+T.GE);
                return T.GE;// +", "+buf;//final state GE
            case 9:
                System.out.println(", "+T.LT );
                return T.LT;// +", "+buf;//final state LT
            case 10:
                System.out.println(", "+T.LE );
                return T.LE;// +", "+buf;//final state LE
            case 11:
                System.out.println(", "+T.NE );
                return T.NE;// +", "+buf;//final state NE
            case 12:
                if (buf.equals("=")){
                    System.out.println(", "+T.EQUAL );
                    return T.EQUAL;// +", "+buf;
                }
                if (buf.equals("+")){
                    System.out.println(", "+T.PLUS );
                    return T.PLUS;// +", "+buf;
                }
                if (buf.equals("-")){
                    System.out.println(", "+T.MINUS );
                    return T.MINUS;// +", "+buf;
                }
                if (buf.equals("/")){
                    System.out.println(", "+T.DIV );
                    return T.DIV;// +", "+buf;
                }
                if (buf.equals("*")){
                    System.out.println(", "+T.TIMES);
                    return T.TIMES;// +", "+buf;
                }
                if (buf.equals("%")){
                    System.out.println(", "+T.MOD );
                    return T.MOD;// +", "+buf;
                }
                if (buf.equals(",")){
                    System.out.println(", "+T.COMMA );
                    return T.COMMA;// +", "+buf;
                }
                if (buf.equals(";")){
                    System.out.println(", "+T.SEMI );
                    return T.SEMI;// +", "+buf;
                }
                if (buf.equals("(")){
                    System.out.println(", "+T.LPAREN );
                    return T.LPAREN;// +", "+buf;
                }
                if (buf.equals(")")){
                    System.out.println(", "+T.RPAREN);
                    return T.RPAREN;// +", "+buf;
                }
                if (buf.equals(".")){
                    System.out.println(", "+T.PERIOD );
                    return T.PERIOD;// +", "+buf;
                }
            case 13:
                // System.out.println("<-- ERROR -- ILLEGAL CHARACTER" + buf);
                return 13;//"Final state, scanner error, character.";
            case 14:
                // System.out.println("ERROR -- STRING NOT TERMINATED");
                return 14;//"Final state, scanner error, character.";
            default:
                // System.out.println("ERROR");
                return 15;
        }
    }

}