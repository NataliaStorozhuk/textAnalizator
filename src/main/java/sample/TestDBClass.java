package sample;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testng.annotations.Test;
import sample.DBModels.Book;
import sample.DBModels.Genre;
import sample.DBModels.Info;
import sample.DBModels.User;
import sample.Services.GenreService;
import sample.Services.UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class TestDBClass {
    public static final String DB_URL = "jdbc:h2:/c:/JavaPrj/SQLDemo/db/mydb";
    public static final String DB_Driver = "org.h2.Driver";

    @Test
    public void testConnection() {
        try {
            Class.forName(DB_Driver); //Проверяем наличие JDBC драйвера для работы с БД
            Connection connection = DriverManager.getConnection(DB_URL);//соединениесБД
            System.out.println("Соединение с СУБД выполнено.");
            connection.close();       // отключение от БД
            System.out.println("Отключение от СУБД выполнено.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("Ошибка SQL !");

        }
    }


    @Test
    public void testNewGenre() {

        SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        try {
            persistGenre(sessionFactory);
            load(sessionFactory);
        } finally {
            sessionFactory.close();
        }
    }

    @Test
    public void testNewBook() {

        SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        try {
            persistBook(sessionFactory);
            load(sessionFactory);
        } finally {
            sessionFactory.close();
        }
    }

    @Test
    public void testNewUserExample() {

        SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        try {
            persistUser(sessionFactory);
            load(sessionFactory);
        } finally {
            sessionFactory.close();
        }
    }

    private static void load(SessionFactory sessionFactory) {
        System.out.println("-- loading persons --");
        Session session = sessionFactory.openSession();
        @SuppressWarnings("unchecked")
        List<Genre> genres = session.createQuery("FROM Genre").list();
        genres.forEach((x) -> System.out.printf("- %s%n", x));

        List<Book> books = session.createQuery("FROM Book").list();
        books.forEach((x) -> System.out.printf(books.toString()));

        List<User> users = session.createQuery("FROM User").list();
        users.forEach((x) -> System.out.printf("- %s%n", x));

        List<Info> info = session.createQuery("FROM Info").list();
        info.forEach((x) -> System.out.printf("- %s%n", x));

        session.close();
    }

    private static void persistGenre(SessionFactory sessionFactory) {


        Genre p1 = new Genre("Путь к файлу", "Имя жанра");
        System.out.println(p1.toString());

        GenreService genreService = new GenreService();
        genreService.saveGenre(p1);

    }

    private static void persistBook(SessionFactory sessionFactory) {
        GenreService genreService = new GenreService();
        Genre p1 = genreService.findGenre(1);
        p1.addBook(new Book("Название книги", "Путь к книге", true));
        genreService.updateGenre(p1);

    }

    private static void persistUser(SessionFactory sessionFactory) {

        UserService userService = new UserService();
        User admin = new User("admin", "admin", true);
        userService.saveUser(admin);
        User operator = new User("operator", "operator", false);
        userService.saveUser(admin);


    }


}
