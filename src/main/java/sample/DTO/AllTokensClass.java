package sample.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AllTokensClass {

    public List<String> arrayAfterSort;
    public List<Double> idf;
    public List<Double> df;
    public List<Double> w;

    public AllTokensClass() {
        this.arrayAfterSort = new ArrayList<>();
        this.idf = new ArrayList<>();
        this.w = new ArrayList<>();
    }
}
