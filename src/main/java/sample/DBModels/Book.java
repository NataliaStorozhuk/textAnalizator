package sample.DBModels;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "book")
@Setter
@Getter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBook;

    @Column(name = "name")
    private String name;

    @Column(name = "filePath")
    private String filePath;

    @Column(name = "indexed")
    private Boolean indexed;

    @Column(name = "training")
    private Boolean training;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idGenre")
    public Genre idGenre;

    public Book() {
    }

    public Book(String name, String filePath, Boolean indexed, Boolean study) {
        this.name = name;
        this.filePath = filePath;
        this.indexed = indexed;
        this.training = study;
    }

    public String toString() {
        return "models.Book{" +
                "id=" + idBook +
                ", name='" + name + '\'' +
                ", filePath='" + filePath + '\'' +
                ", indexed='" + indexed + '\'' +
                ", study='" + training + '\'' +
                ", genre=" + idGenre +
                '}';
    }
}
