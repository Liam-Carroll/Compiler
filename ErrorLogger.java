import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

public class ErrorLogger{
    public PrintWriter out;
    private String fileName;
    public ErrorLogger(String outputFileName){
        try{
            fileName = outputFileName;
            out = new PrintWriter("outputLogs.txt");
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            out.write("Log opened at: "+timestamp);//clears file, leaving only this.
        } catch (IOException e) {
            System.out.println("ErrorLogger:: An error occurred creating file "+outputFileName+" in ErrorLogger constructor");
            e.printStackTrace();
          }
    }
    public void log(String stringPassed){
        try {
            FileWriter logger = new FileWriter(fileName);
            logger.append(stringPassed);
            logger.close();
            // System.out.println("Successfully logged to the output file.");
          } catch (IOException e) {
            System.out.println("An error occurred while logging output file.");
            e.printStackTrace();
          }
    }
    
    public void customError(String errorString, int line ) {
        String fullErrorString = errorString + " at line "+line;
        System.out.println(fullErrorString);
        log(fullErrorString);
        System.exit(0);//kill process after first error is logged
    }
}
