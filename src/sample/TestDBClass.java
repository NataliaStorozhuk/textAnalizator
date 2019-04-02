package sample;

import org.testng.annotations.Test;
import sample.DBModels.Book;
import sample.DBModels.Genre;
import sample.Services.GenreService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBClass {
    public static final String DB_URL = "jdbc:h2:/c:/JavaPrj/SQLDemo/db/diploma";
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
    public void testNewDatabase() {
        GenreService GenreService = new GenreService();
        Genre Genre = new Genre("Документалка", "туть путь");
        GenreService.saveGenre(Genre);

        Book first = new Book("Книга 1");
        first.setGenre(Genre);
        Genre.addBook(first);

        Book second = new Book("Ford");
        second.setGenre(Genre);
        Genre.addBook(second);
        GenreService.updateGenre(Genre);
    }

}
