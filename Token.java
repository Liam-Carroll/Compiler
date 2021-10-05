public class Token {
    public int tokenType;
    public int value; //address in symbol table or numerical value

    public Token(){
        tokenType = 0;
        value = 0;
    }
    public Token(int tokenTypePassed, int valuePassed){
        tokenType = tokenTypePassed;
        value = valuePassed;
    }
}