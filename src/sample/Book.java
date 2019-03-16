package sample;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Book {

    public String name;


    public ArrayList<String> lexems;
    public ArrayList<Integer> tf;
    public ArrayList<Double> tf_idf;

    public Book(String name, ArrayList<String> lexems) {
        this.name = name;
        this.lexems = lexems;
    }

}
