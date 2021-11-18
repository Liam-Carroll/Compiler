public class testQuads {
    public static void main(String[] args){
        Quad q1 = new Quad("ADD", "X", "Y", "Z");
        q1.print();
        Quad q2 = new Quad("BR",null, null, "X");
        q2.print();
        Quad q3 = new Quad("DCL",null, null, "Z");
        q3.print();
    }
}
