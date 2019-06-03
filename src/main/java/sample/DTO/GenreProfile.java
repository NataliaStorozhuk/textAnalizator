package sample.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;


@AllArgsConstructor
@Data
public class GenreProfile {

    public ArrayList<GenreLexema> genreLexemas;

    public GenreProfile(boolean a) {
        this.genreLexemas = new ArrayList<>();
    }

    public GenreProfile() {
    }


    public ArrayList<Double> getWArray() {
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < genreLexemas.size(); i++)
            result.add(genreLexemas.get(i).getW());
        return result;
    }

    public ArrayList<String> getLexemasArray() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < genreLexemas.size(); i++)
            result.add(genreLexemas.get(i).getLexema());
        return result;
    }


    public ArrayList<Double> getIdfArray() {
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < genreLexemas.size(); i++)
            result.add(genreLexemas.get(i).getIdf());
        return result;
    }

    public void setDf(ArrayList<Double> documentFrequency) {
        for (int i = 0; i < documentFrequency.size(); i++)
            this.genreLexemas.get(i).df = documentFrequency.get(i);
    }

    public void setIdf(ArrayList<Double> idf) {
        for (int i = 0; i < idf.size(); i++)
            this.genreLexemas.get(i).idf = idf.get(i);

    }

    public void setW(ArrayList<Double> w) {
        for (int i = 0; i < w.size(); i++)
            this.genreLexemas.get(i).w = w.get(i);

    }
}