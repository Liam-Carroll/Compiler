package Scanner;

public class StringTable {
    private String[] table;

    public StringTable() {
        table = new String[100];
    }

    public int search(String string){
        for(int i = 0; i < table.length; i++){
            if(table[i].equals(string))
                return i;
        }
        return -1;
    }

    public int insert(String string) {
        for(int i = 0; i < table.length; i ++){
            if(table[i].equals(string))
                return i;
            else if(table[i] == null){
                table[i] = string;
                return i;
            }
        }//end for 
        System.out.println("ERROR");
        return -1;
    }
}
