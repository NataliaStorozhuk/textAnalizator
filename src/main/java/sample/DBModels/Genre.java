package sample.DBModels;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "genre")
@Setter
@Getter
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer idGenre;

    @Column(name = "filePath")
    private String filePath;

    @Column(name = "nameGenre")
    private String nameGenre;

    @OneToMany(mappedBy = "idGenre", cascade = CascadeType.ALL, orphanRemoval = false)
    public List<Book> books;

    public Genre() {
    }

    public Genre(String nameGenre, String filePath) {
        this.filePath = filePath;
        this.nameGenre = nameGenre;
        this.books = new ArrayList<>();
    }


    public void addBook(Book book) {
        book.setIdGenre(this);
        this.books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }


    public String toString() {
        return "models.Genre{" +
                "id=" + idGenre +
                ", nameGenre='" + nameGenre + '\'' +
                ", filePath=" + filePath + '}';
    }


}
