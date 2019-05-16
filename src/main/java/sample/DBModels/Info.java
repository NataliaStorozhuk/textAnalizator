package sample.DBModels;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "info")
@Setter
@Getter
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idInfo;

    @Column(name = "stopWordsPath")
    private String stopWordsPath;

    @Column(name = "stopLexemesPath")
    private String stopLexemesPath;

    @Column(name = "precision")
    private Double precision;


    public Info() {
    }


    public String toString() {
        return "models.Info{" +
                "id=" + idInfo +
                ", stopWordsPath='" + stopWordsPath + '\'' +
                ", precision='" + precision + '\'' +
                ", stopLexemesPath=" + stopLexemesPath +
                '}';
    }
}
