package sample.Services;

import javafx.collections.ObservableList;
import sample.DAO.UserDao;
import sample.DBModels.Lexema;
import sample.DBModels.User;

import java.util.List;

public class UserService {

    private UserDao userDao = new UserDao();

    public UserService() {
    }

    public static void saveChanges(ObservableList<Lexema> wordsData, String stopWordsPath) {

    }

    public static User findUser(int id) {
        return UserDao.findById(id);
    }

    public static void saveUser(User User) {
        UserDao.save(User);
    }

    public static void deleteUser(User User) {
        UserDao.delete(User);
    }

    public static void updateUser(User User) {
        UserDao.update(User);
    }

    public static List<User> findAllUsers() {
        return UserDao.findAll();
    }
}
