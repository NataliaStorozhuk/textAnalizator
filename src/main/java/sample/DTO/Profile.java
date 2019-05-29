package sample.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Profile {

    public List<String> arrayAfterSort;
    public List<Double> idf;
    public List<Double> df;
    public List<Double> w;

    public Profile() {
        this.arrayAfterSort = new ArrayList<>();
        this.idf = new ArrayList<>();
        this.w = new ArrayList<>();
    }
}
