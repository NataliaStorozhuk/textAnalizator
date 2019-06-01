package sample.DBModels;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Lexema {
    private String lexema;

    public Lexema(String s) {
        this.lexema = s;
    }
}
