import java.util.ArrayList;
import java.util.Optional;

public class Quads {
    ArrayList<Quad> quads;
    public Quads(){
        quads = new ArrayList<Quad>();
    }
    public int size(){
        return quads.size();
    }
    public void insertQuad(String _operator, String _arg1, String _arg2, String _result){
        //parse args
        String[] quadArgs = parseQuadArgs(_operator, _arg1, _arg2, _result);
        //create quad
        Quad newQuad = new Quad(quadArgs[0], quadArgs[1], quadArgs[2], quadArgs[3]);
        //add to quads arraylist
        quads.add(newQuad);
    }
    public void updateQuad(String _operator, String _arg1, String _arg2, String _result, int index){
        //parse args
        String[] quadArgs = parseQuadArgs(_operator, _arg1, _arg2, _result);

        //Get the quad to update
        Quad edittingQuad = quads.get(index);

        //edit it
        edittingQuad.operator = quadArgs[0];
        edittingQuad.arg1 = quadArgs[1];
        edittingQuad.arg2 = quadArgs[2];
        edittingQuad.result = quadArgs[3];

        //set the quad at that index to the editted quad
        quads.set(index, edittingQuad);
    }
    public void printQuads(){
        for (int i = 0; i< size(); i++){
            quads.get(i).print();
        }
    }
    private String[] parseQuadArgs(String _operator, String _arg1, String _arg2, String _result){
        String operator = parseQuadArg(_operator);
        String arg1 = parseQuadArg(_arg1);
        String arg2 = parseQuadArg(_arg2);
        String result = parseQuadArg(_result);
        return new String[]{operator, arg1, arg2, result};
    }
    private String parseQuadArg(String inputArg){//ADD number indicator * here (by overloading with an int input)
        //Empty and Null coalescing
        // is input null? set output to -, otherwise set it to input
        String outputArg = inputArg == null ? "-" : inputArg;
        // is input blank? set it to -, otherwise keep it the same
        outputArg = outputArg.equals("") ? "-" : outputArg;
        return outputArg;
    }

    //Getters and setters can be done by: quads.get(index).<desired characteristic> = <<new characteristic>>
}
