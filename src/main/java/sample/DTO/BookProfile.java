package sample.DTO;

import lombok.Data;

import java.util.ArrayList;

@Data
public class BookProfile {

    public String name;

    public ArrayList<String> lexems;
    public ArrayList<Integer> tf;
    public ArrayList<Double> tf_idf;
    public ArrayList<Double> normTF;
    private ArrayList<Double> w;

    public BookProfile(String name, ArrayList<String> lexems) {
        this.name = name;
        this.lexems = lexems;
    }

    public BookProfile(String averageBook) {
    }

}
