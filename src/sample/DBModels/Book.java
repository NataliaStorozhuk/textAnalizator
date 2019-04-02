package sample.DBModels;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="book")
@Setter
@Getter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBook;

    @Column(name="name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idGenre")
    private Genre genre;

    public Book() {
    }

    public Book(String name) {
        this.name = name;
    }


    public String toString() {
        return "models.Book{" +
                "id=" + idBook +
                ", name='" + name + '\'' +
                ", genre=" + genre +
                '}';
    }
}
