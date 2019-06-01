package sample.DTO;

import lombok.Data;

@Data
public class GenreLexema {

    String lexema;
    Double idf;
    Double df;
    Double w;

    public GenreLexema(String lexema) {
        this.lexema = lexema;
        this.idf = 0.0;
        this.df = 0.0;
        this.w = 0.0;
    }

    public GenreLexema(String lexema, Double w) {
        this.lexema = lexema;
        this.idf = 0.0;
        this.df = 0.0;
        this.w = w;
    }
}
