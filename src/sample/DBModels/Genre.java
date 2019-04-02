package sample.DBModels;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "genre")
@Setter
@Getter
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idGenre")
    private int idGenre;

    @Column(name = "filePath")
    private String filePath;

    @Column(name = "nameGenre")
    private String nameGenre;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books;

    public Genre() {
    }

    public Genre(String filePath, String nameGenre) {
        this.filePath = filePath;
        this.nameGenre = nameGenre;
    }


    public void addBook(Book book) {
        book.setGenre(this);
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }


    public String toString() {
        return "models.Genre{" +
                "id=" + idGenre +
                ", nameGenre='" + nameGenre + '\'' +
                ", filePath=" + filePath +
                "counBooks=" + books.size() +
                '}';
    }
}
