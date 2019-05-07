package sample.Services;

import sample.DAO.GenreDao;
import sample.DBModels.Book;
import sample.DBModels.Genre;

import java.util.List;

public class GenreService {

    private GenreDao genreDao = new GenreDao();

    public GenreService() {
    }

    public Genre findGenre(int id) {
        return GenreDao.findById(id);
    }

    public void saveGenre(Genre Genre) {
        GenreDao.save(Genre);
    }

    public void deleteGenre(Genre Genre) {
        GenreDao.delete(Genre);
    }

    public void updateGenre(Genre Genre) {
        GenreDao.update(Genre);
    }

    public List<Genre> findAllGenres() {
        return GenreDao.findAll();
    }

    public Book findAutoById(int id) {
        return GenreDao.findBookById(id);
    }
}