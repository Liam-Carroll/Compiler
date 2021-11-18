import java.util.Optional;

public class Quad {
    public String operator, arg1, arg2, result;
    /* input null if no arg 
    EXAMPLE:
     Quad("BR", null, null,"Y") --> (BR - - Y)
    */
    public Quad(String _operator, String _arg1, String _arg2, String _result){
        operator = _operator;
        
        // Similar to: _arg1 == null ? arg1="-" : arg1 = _arg1, trying better practice null coalescing
        Optional<String> optionalArg1 = Optional.ofNullable(_arg1);
        optionalArg1.ifPresentOrElse(
            x -> arg1 = _arg1, 
            () -> arg1 = "-");
        
        Optional<String> optionalArg2 = Optional.ofNullable(_arg2);
        optionalArg2.ifPresentOrElse(
            x -> arg2 = _arg2, 
            () -> arg2 = "-");
        
        Optional<String> optionalResult = Optional.ofNullable(_result);
        optionalResult.ifPresentOrElse(
            x -> result = _result, 
            () -> result = "-");
    }
    public void print(){
        System.out.println(operator +" "+ arg1 + " " + arg2 + " " + result);
    }
    public String returnFullString(){
        return operator +" "+ arg1 + " " + arg2 + " " + result;
    }
}
