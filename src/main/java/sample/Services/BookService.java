package sample.Services;

import sample.DAO.BookDao;
import sample.DBModels.Book;

import java.util.List;

public class BookService {

    private BookDao bookDao = new BookDao();

    public BookService() {
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
