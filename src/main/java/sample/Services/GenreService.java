package sample.Services;

import sample.DAO.GenreDao;
import sample.DBModels.Genre;

import java.util.List;

public class GenreService {

    private GenreDao genreDao = new GenreDao();

    public GenreService() {
    }

    public static Genre findGenre(int id) {
        return GenreDao.findById(id);

    }

    public static void saveGenre(Genre Genre) {
        GenreDao.save(Genre);
    }

    public static void deleteGenre(Genre Genre) {
        GenreDao.delete(Genre);
    }

    public void updateGenre(Genre Genre) {
        GenreDao.update(Genre);
    }

    public static List<Genre> findAllGenres() {
        return GenreDao.findAll();
    }
}
