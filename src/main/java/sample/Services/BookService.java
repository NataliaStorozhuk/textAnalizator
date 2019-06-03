package sample.Services;

import sample.DAO.BookDao;
import sample.DBModels.Book;
import sample.DBModels.Genre;

import java.util.List;

public class BookService {

    private BookDao bookDao = new BookDao();

    public BookService() {
    }

    public static List<Book> getBooksWithGenre(Genre genre) {
        return BookDao.findWithGenre(genre);
    }

    public static List<Book> getIndexedBooksWithGenre(Genre genre) {
        return BookDao.findIndexedWithGenre(genre);
    }

    public static List<Book> getTrainingBooksWithGenre(Genre genre) {
        return BookDao.findTrainingWithGenre(genre);
    }

    public Book findBook(int id) {
        return BookDao.findById(id);
    }

    public void saveBook(Book Book) {
        BookDao.save(Book);
    }

    public void deleteBook(Book Book) {
        BookDao.delete(Book);
    }

    public void updateBook(Book Book) {
        BookDao.update(Book);
    }

    public List<Book> findAllBooks() {
        return BookDao.findAll();
    }
}
