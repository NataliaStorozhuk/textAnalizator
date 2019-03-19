package sample;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AllTokensClass {

    List<String> arrayAfterSort;
    List<Double> idf;
    List<Double> w;

    public AllTokensClass() {
        this.arrayAfterSort = new ArrayList<>();
        this.idf = new ArrayList<>();
        this.w = new ArrayList<>();
    }
}
