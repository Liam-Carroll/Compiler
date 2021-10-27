package Scanner;

import java.io.IOException;
import java.util.Scanner;

public class RunProgram {

	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);
        System.out.print("Enter program file name: ");
        String fileName = input.nextLine();

        CompilerScanner scan = new CompilerScanner(fileName); // now needs symboltable and string table

        Token t = new Token();
        while(t.tokenType != T.PERIOD ){
            try{
                t = scan.nextToken();
                if (t == null){//error 
                    break;
                }
            }catch (Exception e){

            }
            
            
        }
        scan.out.close();
        input.close();

	}

}
