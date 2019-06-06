package sample.FileConverter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Analizator.StatisticGetter;
import sample.DBModels.Book;
import sample.DBModels.Genre;
import sample.DTO.BookProfile;
import sample.DTO.GenreProfile;
import sample.DTO.Lexema;
import sample.Services.BookService;
import sample.Services.GenreService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static sample.FileConverter.ObjectToJsonConverter.fromJsonToObject;
import static sample.FileConverter.TxtToBookConverter.*;

public class JsonFileCreator {

    public static void updateFileStopWords(ObservableList<Lexema> lexemas, String pathFile) throws IOException {
        FileWriter nFile = new FileWriter(pathFile);

        for (Lexema lexema : lexemas) {
            nFile.write(lexema.getLexema() + "\n");

        }
        nFile.close();
    }

    public static ObservableList<Lexema> getFromFileStopWords(String pathFile) {
        ObservableList<Lexema> wordsData = FXCollections.observableArrayList();

        String stopWordsString = usingBufferedReader(pathFile);
        ArrayList<String> stopWords = getWordsFromString(stopWordsString);
        for (String stopWord : stopWords) {
            wordsData.add(new Lexema(stopWord));
        }
        return wordsData;
    }

    public static Book addNewBookJsonInDB(String applicationPath, Genre currentGenre, String newPath) {
        //сделали файлик
        TxtToBookConverter fileToBookConverter = new TxtToBookConverter();
        BookProfile resultBook = fileToBookConverter.getBookProfileFromTxt(new File(newPath));


        File directory = new File(applicationPath + "books\\" + currentGenre.getNameGenre());

        if (!directory.exists()) {
            directory.mkdir();
        }

        String resultPath = (applicationPath + "books\\" + currentGenre.getNameGenre() + "\\" + resultBook.getName() + ".json");
        ObjectToJsonConverter.fromObjectToJson(resultPath, resultBook);

        //записали в базочку
        BookService bookService = new BookService();
        Book newBook = new Book(resultBook.name, resultPath, false, false, currentGenre);
        bookService.saveBook(newBook);
        return newBook;
    }

    public static void genreTraining(List<Book> booksFromBase, String applicationPath, String newName) {
        ArrayList<BookProfile> booksFromJson = new ArrayList<>();    //взяли книжечки исходные
        for (Book book : booksFromBase) {
            booksFromJson.add((BookProfile) fromJsonToObject(book.getFilePath(), BookProfile.class));
            book.setIndexed(true);
            BookService.updateBook(book);
        }

        //Получаем показатели все по выборке
        GenreProfile testViborka = StatisticGetter.getBaseFrequencies(booksFromJson); //это очень долго бежит
        //Пишем в gson
        ObjectToJsonConverter.fromObjectToJson(applicationPath + "genres\\" +
                newName + ".json", testViborka);
    }

    public static Genre addNewGenreJsonAndDB(String newName, File folder, String applicationPath) {
        String genrePath = (applicationPath + "genres\\");
        String booksPath = (applicationPath + "books\\" +
                newName);


        //Создаем жанр в базе сразу
        Genre newGenre = new Genre(newName, genrePath + newName + ".json");
        GenreService.saveGenre(newGenre);


        //Получаем книги и пишем их в фс
        ArrayList<BookProfile> books = getBookProfilesInJsonAndDB(booksPath, newGenre, folder);

        //Получаем показатели все по выборке
        GenreProfile testViborka = StatisticGetter.getBaseFrequencies(books);

        //Пишем жанр в gson
        ObjectToJsonConverter.fromObjectToJson(applicationPath + "genres\\" +
                newName + ".json", testViborka);
        return newGenre;
    }

    private static ArrayList<BookProfile> getBookProfilesInJsonAndDB(String bookPath, Genre newGenre, File folder) {
        ArrayList<BookProfile> books = listBooksFromFolder(folder);

        File directory = new File(bookPath);

        if (!directory.exists()) {
            directory.mkdir();
        }

        for (BookProfile book : books) {
            String resultPath = (bookPath + "\\" + book.getName() + ".json");
            ObjectToJsonConverter.fromObjectToJson(resultPath, book);

            //записали в базочку
            Book newBook = new Book(book.name, resultPath, true, true, newGenre);
            BookService.saveBook(newBook);
        }
        return books;
    }

}