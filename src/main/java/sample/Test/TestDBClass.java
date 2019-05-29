package sample.Test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testng.annotations.Test;
import sample.DBModels.Book;
import sample.DBModels.Genre;
import sample.DBModels.Info;
import sample.DBModels.User;
import sample.Services.GenreService;
import sample.Services.InfoService;
import sample.Services.UserService;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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
    public void testNewUserExample() throws NoSuchAlgorithmException, UnsupportedEncodingException {

        SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        try {
            persistUser(sessionFactory);
            load(sessionFactory);
        } finally {
            sessionFactory.close();
        }
    }

    @Test
    public void testNewInfoExample() {

        SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        try {
            persistInfo(sessionFactory);
            load(sessionFactory);
        } finally {
            sessionFactory.close();
        }
    }

    @Test
    public void testBookSelectionExample() {

        SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        try {
            persistInfoBooksSelection(sessionFactory);
            //    load(sessionFactory);
        } finally {
            sessionFactory.close();
        }
    }

    //TODO ЭТО ОБЯЗАТЕЛЬНО ПОТЕСТИТЬ и dao и сервисы можно упростить и тогда и в мейне это убрать
    private static void load(SessionFactory sessionFactory) {
      /*  System.out.println("-- loading persons --");
        Session session = sessionFactory.openSession();
        @SuppressWarnings("unchecked")
        List<Genre> genres = session.createQuery("FROM Genre").list();
        genres.forEach((x) -> System.out.printf("- %s%n", x));*/
        List<Genre> genres = GenreService.findAllGenres();
        genres.forEach((x) -> System.out.printf("- %s%n", x));

/*        List<Book> books = session.createQuery("FROM Book").list();
        books.forEach((x) -> System.out.printf(books.toString()));

                List<User> users = session.createQuery("FROM User").list();
        users.forEach((x) -> System.out.printf("- %s%n", x));

        List<Info> info = session.createQuery("FROM Info").list();
        info.forEach((x) -> System.out.printf("- %s%n", x));
*/
        //  session.close();
    }

    private static void persistGenre(SessionFactory sessionFactory) {


        Genre p1 = new Genre("Имя жанра 5", "Путь к файлу 5");
        System.out.println(p1.toString());

        GenreService genreService = new GenreService();
        genreService.saveGenre(p1);

    }

    private static void persistBook(SessionFactory sessionFactory) {
        GenreService genreService = new GenreService();
        Genre p1 = genreService.findGenre(1);
        //   p1.addBook(new Book("Не обуч, не инд 1 ", "Путь к кн24ге", false, false));
        //    p1.addBook(new Book("Не обуч, инд 1 ", "Путь к к2иге", true, false));
        //    p1.addBook(new Book("Не обуч, инд 2 ", "Путь к кн1иге", true, false));
        //   p1.addBook(new Book("Обуч, не инд 1", "Путь к 1книге", false, true));
        p1.addBook(new Book("Оба", "Путь к к3ниге", true, true));
        genreService.updateGenre(p1);

    }

    private static void persistInfoBooksSelection(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();

        List<Genre> genres = session.createQuery("FROM Genre").list();

        //только обучающие
        List<Book> books = session.createQuery("FROM Book book where book.training=true and book.idGenre =:testParam").setParameter("testParam", genres.get(0)).list();


        //только проиндексированные
        // List<Book> books = session.createQuery("FROM Book book where book.indexed=true and book.idGenre =:testParam").setParameter("testParam", genres.get(0)).list();

        //все
        // List<Book> books = session.createQuery("FROM Book book where book.idGenre =:testParam").setParameter("testParam", genres.get(0)).list();
        books.forEach((x) -> System.out.printf("- %s%n", x));
    }

    private static void persistUser(SessionFactory sessionFactory) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        UserService userService = new UserService();

        //     userService.deleteUser(userService.findUser(1));

        String hashPassword = String.valueOf(("admin").hashCode());

        User admin = new User("admin", hashPassword, true);
        userService.saveUser(admin);

        hashPassword = String.valueOf(("operator").hashCode());
        User operator = new User("operator", hashPassword, false);
        userService.saveUser(operator);


    }

    private static void persistInfo(SessionFactory sessionFactory) {

        InfoService infoService = new InfoService();
        Info info = new Info("путь к стоп-словам", "путь к стоп-лексемам", 0.7, 1000.0);
        infoService.saveInfo(info);
    }


}
