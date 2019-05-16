package sample.Services;

import sample.DAO.UserDao;
import sample.DBModels.User;

import java.util.List;

public class UserService {

    private UserDao userDao = new UserDao();

    public UserService() {
    }

    public User findUser(int id) {
        return UserDao.findById(id);
    }

    public void saveUser(User User) {
        UserDao.save(User);
    }

    public void deleteUser(User User) {
        UserDao.delete(User);
    }

    public void updateUser(User User) {
        UserDao.update(User);
    }

    public List<User> findAllUsers() {
        return UserDao.findAll();
    }
}
